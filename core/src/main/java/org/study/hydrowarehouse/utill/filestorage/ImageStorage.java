package org.study.hydrowarehouse.utill.filestorage;

import org.springframework.web.multipart.MultipartFile;
import org.study.hydrowarehouse.exception.CoreException;

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
     * @param uploadedFileSize the size of the uploaded file in bytes.
     * @throws CoreException if the size file is bigger than its allowed.
     */
    void isBigSizeFile(long uploadedFileSize) throws CoreException;

    /**
     * Removes a file from the system based on the provided file path. This method attempts to delete the file
     * located at the specified path.
     * @param path The absolute path to the file that should be removed.
     *              The path should be in a valid format and point to an existing file.
     * @return {@code true} if the file was successfully removed,
     *         {@code false} otherwise.
     * @throws CoreException If the provided path is null or empty.
     */
    boolean removeFile(String path) throws CoreException;

    /**
     * Checks whether the file name length (in characters) exceeds a predefined maximum limit.
     * @param length the length of the file name in bytes
     * @throws CoreException if the name's file is bigger than its allowed.
     */
    void isBigLengthName(int length);

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
