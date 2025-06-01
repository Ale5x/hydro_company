package org.study.hydrowarehouse.utill.filestorage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.study.hydrowarehouse.exception.CoreException;

import static org.junit.jupiter.api.Assertions.*;

class FileNameValidatorImplTest {

    private FileNameValidatorImpl validator;

    @BeforeEach
    void setUp() {
        validator = new FileNameValidatorImpl();
    }

    @Test
    void validateFileName_shouldThrowException_whenFileNameIsNull() {
        CoreException ex = assertThrows(CoreException.class,
                () -> validator.validateFileName(null));
        assertEquals("File name is empty.", ex.getMessage());
    }

    @Test
    void validateFileName_shouldThrowException_whenFileNameIsBlank() {
        CoreException ex = assertThrows(CoreException.class,
                () -> validator.validateFileName("   "));
        assertEquals("File name is empty.", ex.getMessage());
    }

    @Test
    void validateFileName_shouldPass_whenFileNameIsValid() {
        assertDoesNotThrow(() -> validator.validateFileName("image.jpg"));
    }

    @Test
    void validateAllowedExtension_shouldThrowException_whenExtensionIsNull() {
        CoreException ex = assertThrows(CoreException.class,
                () -> validator.validateAllowedExtension(null));
        assertTrue(ex.getMessage().contains("File has the wrong extension."));
    }

    @Test
    void validateAllowedExtension_shouldThrowException_whenExtensionIsBlank() {
        CoreException ex = assertThrows(CoreException.class,
                () -> validator.validateAllowedExtension("   "));
        assertTrue(ex.getMessage().contains("File has the wrong extension."));
    }

    @Test
    void validateAllowedExtension_shouldThrowException_whenExtensionIsNotAllowed() {
        String badExtension = ".exe";
        CoreException ex = assertThrows(CoreException.class,
                () -> validator.validateAllowedExtension(badExtension));
        assertTrue(ex.getMessage().contains("File has the wrong extension."));
        assertTrue(ex.getMessage().contains(badExtension));
    }

    @Test
    void validateAllowedExtension_shouldPass_whenExtensionIsAllowed_lowercase() {
        assertDoesNotThrow(() -> validator.validateAllowedExtension(".jpg"));
        assertDoesNotThrow(() -> validator.validateAllowedExtension(".jpeg"));
        assertDoesNotThrow(() -> validator.validateAllowedExtension(".png"));
        assertDoesNotThrow(() -> validator.validateAllowedExtension(".webp"));
    }

    @Test
    void validateAllowedExtension_shouldPass_whenExtensionIsAllowed_uppercase() {
        assertDoesNotThrow(() -> validator.validateAllowedExtension(".JPG"));
        assertDoesNotThrow(() -> validator.validateAllowedExtension(".JPEG"));
        assertDoesNotThrow(() -> validator.validateAllowedExtension(".PNG"));
        assertDoesNotThrow(() -> validator.validateAllowedExtension(".WEBP"));
    }
}