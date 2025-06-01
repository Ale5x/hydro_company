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
     * Generates a unique name for the given uploaded file.
     *
     * <p>This method creates a unique file name based on the original filename
     * and potentially other factors such as a UUID or timestamp. It is used to prevent
     * filename collisions when storing files.</p>
     *
     * @param file the uploaded file for which to generate a unique name; must not be {@code null}
     * @return a unique filename as a {@link String}, preserving the original file extension if present
     * @throws CoreException if the file is {@code null}, has no original filename, or an error occurs during name generation
     */
    String generateUniqueName(MultipartFile file) throws CoreException;

    /**
     * Checks whether the provided uploaded file exceeds the allowed maximum file size.
     *
     * <p>This method validates the size of the given {@link MultipartFile}. If the file size
     * exceeds the configured limit, a {@link CoreException} is thrown.</p>
     *
     * @param uploadedFile the file to check; must not be {@code null}
     * @throws CoreException if the file size exceeds the maximum allowed limit
     */
    void isBigSizeFile(MultipartFile uploadedFile) throws CoreException;

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
     * Checks if the provided file name exceeds the maximum allowed length.
     * <p>
     * Throws a {@link CoreException} if the file name is {@code null}, empty,
     * or its length is greater than or equal to the configured maximum length.
     *
     * @param fileName the name of the file to check
     * @throws CoreException if {@code fileName} is {@code null} or empty,
     *                       or if its length exceeds the maximum allowed length
     */
    void isBigLengthName(String fileName) throws CoreException;

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

    /**
     * Validates that the provided string is not {@code null} or empty.
     *
     * <p>This method is typically used to ensure that required string input
     * for a specific operation is present and non-empty. If the input string
     * is {@code null} or empty, a {@link CoreException} is thrown with
     * a message related to the provided operation name.</p>
     *
     * @param line the string to validate; may be {@code null} or empty
     * @param nameOperation the name of the operation being validated; used in the exception message
     * @throws CoreException if the input string is {@code null} or empty
     */
    void isEmptyString(String line, String nameOperation) throws CoreException;

    /**
     * Validates that the provided object is not {@code null}.
     *
     * <p>This method checks whether the specified object is {@code null} and
     * throws a {@link CoreException} if it is. The {@code nameOperation} parameter
     * is used to provide context in the exception message, typically indicating
     * which operation or field failed the validation.</p>
     *
     * @param object the object to validate; may be {@code null}
     * @param nameOperation the name or description of the operation being validated
     * @throws CoreException if the provided object is {@code null}
     */
    void isNull(Object object, String nameOperation) throws CoreException;
}
