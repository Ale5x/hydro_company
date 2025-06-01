package org.study.hydrowarehouse.utill.filestorage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.study.hydrowarehouse.exception.CoreException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileNameConverterTest {

    private FileNameValidator validator;
    private FileNameTransliterator transliterator;
    private FileNameFormatter formatter;
    private FileNameConverter converter;

    @BeforeEach
    void setUp() {
        validator = mock(FileNameValidator.class);
        transliterator = mock(FileNameTransliterator.class);
        formatter = mock(FileNameFormatter.class);
        converter = new FileNameConverter(validator, transliterator, formatter);
    }

    @Test
    void sanitizeAndTransliterateFileName_shouldProcessCorrectFileName() {
        String input = "файл с пробелами и_спец#символами.jpg";

        when(transliterator.transliterate("файл с пробелами и_спец#символами"))
                .thenReturn("fail s probelami i_spec#simvolami");
        when(formatter.replaceSpaces("fail s probelami i_spec#simvolami"))
                .thenReturn("fail_s_probelami_i_spec#simvolami");
        when(formatter.removeSpecialCharacters("fail_s_probelami_i_spec#simvolami"))
                .thenReturn("fail_s_probelami_i_spec_simvolami");

        String result = converter.sanitizeAndTransliterateFileName(input);

        assertEquals("fail_s_probelami_i_spec_simvolami.jpg", result);
        verify(validator).validateFileName(input);
        verify(validator).validateAllowedExtension(".jpg");
    }

    @Test
    void sanitizeAndTransliterateFileName_shouldThrowException_whenFileNameIsNull() {
        doThrow(new CoreException("File name is empty."))
                .when(validator).validateFileName(null);

        CoreException ex = assertThrows(CoreException.class,
                () -> converter.sanitizeAndTransliterateFileName(null));

        assertEquals("File name is empty.", ex.getMessage());
    }

    @Test
    void sanitizeAndTransliterateFileName_shouldThrowException_whenFileNameIsBlank() {
        doThrow(new CoreException("File name is empty."))
                .when(validator).validateFileName(" ");

        CoreException ex = assertThrows(CoreException.class,
                () -> converter.sanitizeAndTransliterateFileName(" "));

        assertEquals("File name is empty.", ex.getMessage());
    }

    @Test
    void sanitizeAndTransliterateFileName_shouldThrowException_whenExtensionIsInvalid() {
        String fileName = "some_file.exe";

        doNothing().when(validator).validateFileName(fileName);
        doThrow(new CoreException("Wrong extension - .exe"))
                .when(validator).validateAllowedExtension(".exe");

        CoreException ex = assertThrows(CoreException.class,
                () -> converter.sanitizeAndTransliterateFileName(fileName));

        assertEquals("Wrong extension - .exe", ex.getMessage());
    }

    @Test
    void sanitizeAndTransliterateFileName_shouldWorkWithUppercaseExtension() {
        String input = "image test.JPEG";

        when(transliterator.transliterate("image test")).thenReturn("image test");
        when(formatter.replaceSpaces("image test")).thenReturn("image_test");
        when(formatter.removeSpecialCharacters("image_test")).thenReturn("image_test");

        String result = converter.sanitizeAndTransliterateFileName(input);

        assertEquals("image_test.JPEG", result);
        verify(validator).validateAllowedExtension(".JPEG"); // должно быть приведение к lower внутри валидатора
    }

    @Test
    void sanitizeAndTransliterateFileName_shouldThrowException_whenFileNameHasNoExtension() {
        String fileName = "no_extension";

        doNothing().when(validator).validateFileName(fileName);
        doThrow(new CoreException("Wrong extension - "))
                .when(validator).validateAllowedExtension("");

        CoreException ex = assertThrows(CoreException.class,
                () -> converter.sanitizeAndTransliterateFileName(fileName));

        assertEquals("Wrong extension - ", ex.getMessage());
    }

    @Test
    void sanitizeAndTransliterateFileName_shouldHandleMultipleDots() {
        String fileName = "weird.name.with.dots.png";

        when(transliterator.transliterate("weird.name.with.dots")).thenReturn("weird.name.with.dots");
        when(formatter.replaceSpaces("weird.name.with.dots")).thenReturn("weird.name.with.dots");
        when(formatter.removeSpecialCharacters("weird.name.with.dots")).thenReturn("weird.name.with.dots");

        String result = converter.sanitizeAndTransliterateFileName(fileName);

        assertEquals("weird.name.with.dots.png", result);
        verify(validator).validateAllowedExtension(".png");
    }
}