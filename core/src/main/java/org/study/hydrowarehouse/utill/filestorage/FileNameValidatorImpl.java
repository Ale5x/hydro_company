package org.study.hydrowarehouse.utill.filestorage;

import org.springframework.stereotype.Service;
import org.study.hydrowarehouse.exception.CoreException;

import java.util.List;

/**
 * Implementation of the {@link FileNameValidator} interface.
 *
 * @see FileNameValidator
 *
 * @author Aliaksandr Pishchala
 */
@Service
public class FileNameValidatorImpl implements FileNameValidator {
    private static final String EMPTY_FILE_NAME_MESSAGE = "File name is empty.";
    private static final String WRONG_FILE_EXTENSION_MESSAGE = "File has the wrong extension.";
    private static final List<String> ALLOWED_EXTENSIONS = List.of(".jpg", ".jpeg", ".png", ".webp");

    @Override
    public void validateFileName(String fileName) throws CoreException {
        if (fileName == null || fileName.isBlank()) {
            throw new CoreException(EMPTY_FILE_NAME_MESSAGE);
        }
    }

    @Override
    public void validateAllowedExtension(String extension) throws CoreException {
        if (extension == null || extension.isBlank() || !ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new CoreException(WRONG_FILE_EXTENSION_MESSAGE + " - " + extension);
        }
    }
}
