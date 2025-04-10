package org.study.hydro.utill;

import org.springframework.web.multipart.MultipartFile;
import org.study.hydro.exception.CoreException;

import java.util.List;

/**
 * Interface {@link ImageStorage} for handling image storage operations such as saving files, generating unique file names, and
 * performing validations.
 *
 * @author Aliaksandr Pishchala
 */
public interface ImageStorage {

    /**
     * Saves the uploaded image file to the specified directory.
     * @param file the image file to be saved (uploaded by user)
     * @param uploadDir the directory path where the file should be stored
     * @return the generated file name or path after saving
     * @throws CoreException if saving fails or file is invalid
     */
    String save(MultipartFile file, String uploadDir) throws CoreException;

    /**
     * Generates a unique name for the uploaded file, typically to avoid overwriting files with the same name or
     * to add timestamps, UUIDs, etc.
     * @param fileName the original name of the file
     * @return a unique file name
     * @throws CoreException if generation fails
     */
    String generateUniqueName(String fileName) throws CoreException;

    /**
     * Checks whether the uploaded file size exceeds a predefined maximum limit.
     * @param uploadedFileSize the size of the uploaded file in bytes
     * @return true if the file is too large, false otherwise
     */
    boolean isBigSizeFile(long uploadedFileSize);

    /**
     * Checks whether the file name length (in characters) exceeds a predefined maximum limit.
     * @param length the length of the file name in bytes
     * @return true if the name is too long, false otherwise
     */
    boolean isBigLengthName(byte length);

    /**
     * Adds additional information before the file extension to make unique name.
     * @param fileName the original file name
     * @param additionalText the text to insert before the file extension
     * @return the modified file name with additional text
     */
    String addAdditionalInfoBeforeExtension(String fileName, String additionalText);

    /**
     * Saves all uploaded image files to the specified upload directory.
     * @param files the list of files to save
     * @param uploadDir the target directory where files will be stored
     * @return a list of absolute paths to the saved files
     * @throws CoreException if any file fails to save or if the directory is invalid
     */
    List<String> saveAll(List<MultipartFile> files, String uploadDir) throws CoreException;
}
