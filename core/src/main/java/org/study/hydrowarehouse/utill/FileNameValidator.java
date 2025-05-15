package org.study.hydrowarehouse.utill;

import org.study.hydrowarehouse.exception.CoreException;

/**
 * Interface {@link FileNameValidator} for validating file names and their extensions.
 *
 * @author Aliaksandr Pishchala
 */
public interface FileNameValidator {

    /**
     * Validates the given file name.
     * @param fileName the name of the file to validate
     * @throws CoreException if the file name is invalid
     */
    void validateFileName(String fileName) throws CoreException;

    /**
     * Validates whether the given file extension is allowed.
     * @param extension the file extension to validate (e.g., "pdf", "txt")
     * @throws CoreException if the extension is not allowed
     */
    void validateAllowedExtension(String extension) throws CoreException;
}
