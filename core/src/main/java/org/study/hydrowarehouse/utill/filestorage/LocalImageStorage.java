package org.study.hydrowarehouse.utill.filestorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.study.hydrowarehouse.exception.CoreException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * This implementation of {@link ImageStorage} for local file storage. This service handles the storage of images
 * locally on the disk. It is responsible for saving, validating, and managing file names for images that need to be
 * stored on the local file system.
 *
 * @see ImageStorage
 *
 * @author Aliaksandr Pishchala
 */
@Service
public class LocalImageStorage implements ImageStorage {

   private final FileNameConverter fileNameConvector;

    public static final String OBJECT_IS_NULL_MESSAGE = "Object is null. Operation - %s";
    public static final String LINE_IS_EMPTY_MESSAGE = "Line is empty. Operation - %s";
    public static final String CHECK_FILE_NAME_LENGTH_OPERATION = "Checking file name length";
    public static final String CHECK_FILE_SIZE_BLANK_OPERATION = "Checking file size";
    public static final String CHECK_DIR_OPERATION = "Checking directory";
    public static final String REMOVE_FILE_OPERATION = "Removing file";
    public static final String GENERATE_UNIQUE_NAME_OPERATION = "Generate unique name";
    private final static String FILE_NOT_SAVE_MESSAGE = "The file wasn't save.";
    private final static String MAX_FILE_SIZE_EXCEEDED_MESSAGE = "File size exceeded. The size - ";
    private final static String MAX_NAME_LENGTH_EXCEEDED_MESSAGE = "The file's name length exceeded. The length - ";
    private final static String FILE_PATH_NOT_EXIST_MESSAGE = "File does not exist. Path - %s";
    private final static String FILES_NOT_SAVE_MESSAGE = "Failed to save all files. Rollback completed.";
    private final static String FILED_REMOVING_FILE_MESSAGE = "Filed removing file. Path - %s";
    private final static String FILES_LIMIT_EXCEEDED_MESSAGE = "File upload limit exceeded. Maximum allowed files per request: ";

    @Autowired
    public LocalImageStorage(FileNameConverter fileNameConvector) {
        this.fileNameConvector = fileNameConvector;
    }

    @Value("${file.multipart.max-size-file}")
    private long maxSizeFile;

    @Value("${file.multipart.max-length-file}")
    private int maxLengthFile;

    @Value("${file.multipart.max-allowed-files}")
    private int maxAllowedFiles;


    @Override
    public String save(MultipartFile file, String uploadDir) throws CoreException {
        try {
            isBigSizeFile(file);
            isBigLengthName(file.getOriginalFilename());
            isDir(uploadDir);
            Path filePath = Paths.get(uploadDir + generateUniqueName(file));
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return filePath.toString();
        } catch (IOException e) {
            throw new CoreException(FILE_NOT_SAVE_MESSAGE + e);
        }
    }

    @Override
    public String generateUniqueName(MultipartFile file) throws CoreException {
        isNull(file.getOriginalFilename(), GENERATE_UNIQUE_NAME_OPERATION);
        isEmptyString(file.getOriginalFilename(),GENERATE_UNIQUE_NAME_OPERATION);
        String uniqueFileName  = String.valueOf(System.currentTimeMillis());
        return addAdditionalInfoBeforeExtension(fileNameConvector.sanitizeAndTransliterateFileName(
                file.getOriginalFilename()), uniqueFileName);
    }

    @Override
    public String addAdditionalInfoBeforeExtension(String fileName, String additionalText) throws CoreException {
        int dotIndex = fileName.lastIndexOf(".");
        String baseName = (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
        String extension = (dotIndex == -1) ? "" : fileName.substring(dotIndex);

        return baseName + "_" + additionalText + extension;
    }

    @Override
    public void isBigSizeFile(MultipartFile uploadedFile) throws CoreException {
        isNull(uploadedFile, CHECK_FILE_SIZE_BLANK_OPERATION);
        if (uploadedFile.getSize() >= maxSizeFile) {
            throw new CoreException(String.format(MAX_FILE_SIZE_EXCEEDED_MESSAGE, uploadedFile));
        }
    }

    @Override
    public void isBigLengthName(String fileName) throws CoreException {
        isNull(fileName, CHECK_FILE_NAME_LENGTH_OPERATION);
        isEmptyString(fileName, CHECK_FILE_NAME_LENGTH_OPERATION);
        if (fileName.length() >= maxLengthFile) {
            throw new CoreException(MAX_NAME_LENGTH_EXCEEDED_MESSAGE + fileName.length());
        }
    }

    /**
     * Ensures that the given upload directory exists. If the directory does not exist, it will be created along
     * with any necessary parent directories.
     *
     * @param uploadDir the path to the directory to check or create
     * @throws IOException if an I/O error occurs while creating the directory
     */
    private void isDir(String uploadDir) throws IOException {
        isNull(uploadDir, CHECK_DIR_OPERATION);
        isEmptyString(uploadDir, CHECK_DIR_OPERATION);
        Path path = Paths.get(uploadDir);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }

    @Override
    public void isEmptyString(String line, String nameOperation) throws CoreException {
        isNull(line, nameOperation);
        if (line.isBlank()) {
            throw new CoreException(String.format(LINE_IS_EMPTY_MESSAGE, nameOperation));
        }
    }

    @Override
    public void isNull(Object object, String nameOperation) throws CoreException {
        if (object == null) {
            throw new CoreException(String.format(OBJECT_IS_NULL_MESSAGE, nameOperation));
        }
    }

    @Override
    public List<String> saveAll(List<MultipartFile> files, String uploadDir) throws CoreException {
        List<String> savedPaths = new ArrayList<>();

        if (files.size() > maxAllowedFiles) {
            throw new CoreException(FILES_LIMIT_EXCEEDED_MESSAGE + files.size());
        }
        for (MultipartFile file : files) {
            try {
                String path = save(file, uploadDir);
                savedPaths.add(path);
            } catch (CoreException e) {
                for (String savedPath : savedPaths) {
                    try {
                        Files.deleteIfExists(Paths.get(savedPath));
                    } catch (IOException ioException) {
                        // for logging
                    }
                }
                throw new CoreException(FILES_NOT_SAVE_MESSAGE, e);
            }
        }
        return savedPaths;
    }

    @Override
    public boolean removeFile(String path) throws CoreException {
        isNull(path, REMOVE_FILE_OPERATION);
        isEmptyString(path, REMOVE_FILE_OPERATION);

        Path pathFile = Paths.get(path);
        if (!Files.exists(pathFile) || !Files.isRegularFile(pathFile)) {
            // logger
            throw new CoreException(String.format(FILE_PATH_NOT_EXIST_MESSAGE, path));
        }

        try {
            return Files.deleteIfExists(pathFile);
        } catch (IOException e) {
            // logger
            throw new CoreException(String.format(FILED_REMOVING_FILE_MESSAGE, path));
        }
    }
}
