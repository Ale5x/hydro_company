package org.study.hydro.utill;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.study.hydro.exception.CoreException;

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
 * @author Aliaksandr Pishchala
 */
@Service
public class LocalImageStorage implements ImageStorage {

    private final FileNameConverter fileNameConvector;

    private final static String FILE_NOT_SAVE_ERROR = "The file wasn't save.";
    private final static String FILES_NOT_SAVE_ERROR = "Failed to save all files. Rollback completed.";
    private final static String FILES_LIMIT_EXCEEDED_ERROR = "File upload limit exceeded. Maximum allowed files per request: ";

    @Autowired
    public LocalImageStorage(FileNameConverter fileNameConvector) {
        this.fileNameConvector = fileNameConvector;
    }

    @Value("${file.multipart.max-size-file}")
    private long maxSizeFile;

    @Value("${file.multipart.max-length-file}")
    private byte maxLengthFile;

    @Value("${file.multipart.max-allowed-files}")
    private int maxAllowedFiles;


    @Override
    public String save(MultipartFile file, String uploadDir) throws CoreException {
        try {
            isDir(uploadDir);
            Path filePath = Paths.get(uploadDir + generateUniqueName(file.getOriginalFilename()));
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return filePath.getFileName().toString();
        } catch (IOException e) {
            throw new CoreException(FILE_NOT_SAVE_ERROR + e);
        }
    }

    @Override
    public String generateUniqueName(String fileName) {
        String uniqueFileName  = String.valueOf(System.currentTimeMillis());
        return addAdditionalInfoBeforeExtension(fileNameConvector.sanitizeAndTransliterateFileName(fileName), uniqueFileName);
    }

    @Override
    public String addAdditionalInfoBeforeExtension(String fileName, String additionalText) {
        int dotIndex = fileName.lastIndexOf(".");
        String baseName = (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
        String extension = (dotIndex == -1) ? "" : fileName.substring(dotIndex);

        return baseName + "_" + additionalText + extension;
    }

    @Override
    public boolean isBigSizeFile(long uploadedFileSize ) {
        return maxSizeFile <= uploadedFileSize ;
    }

    @Override
    public boolean isBigLengthName(byte length) {
        return length >= maxLengthFile;
    }

    private void isDir(String uploadDir) throws IOException {
        Path path = Paths.get(uploadDir);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }

    @Override
    public List<String> saveAll(List<MultipartFile> files, String uploadDir) throws CoreException {
        List<String> savedPaths = new ArrayList<>();

        if (files.size() > maxAllowedFiles) {
            throw new CoreException(FILES_LIMIT_EXCEEDED_ERROR + files.size());
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
                throw new CoreException(FILES_NOT_SAVE_ERROR, e);
            }
        }
        return savedPaths;
    }
}
