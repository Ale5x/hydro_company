package org.study.hydrowarehouse.utill.filestorage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileNameFormatterImplTest {

    private FileNameFormatterImpl formatter;

    @BeforeEach
    void setUp() {
        formatter = new FileNameFormatterImpl();
    }

    @Test
    void removeSpecialCharacters_shouldReplaceSpecialCharsWithUnderscore() {
        String input = "my@file#name$.png";
        String expected = "my_file_name_.png";
        String actual = formatter.removeSpecialCharacters(input);
        assertEquals(expected, actual);
    }

    @Test
    void removeSpecialCharacters_shouldNotModifyValidFileName() {
        String input = "valid_file-name.123.png";
        String actual = formatter.removeSpecialCharacters(input);
        assertEquals(input, actual);
    }

    @Test
    void removeSpecialCharacters_shouldHandleEmptyString() {
        String input = "";
        String actual = formatter.removeSpecialCharacters(input);
        assertEquals("", actual);
    }

    @Test
    void removeSpecialCharacters_shouldHandleOnlySpecialCharacters() {
        String input = "!@#$%^&*()";
        String expected = "__________";
        String actual = formatter.removeSpecialCharacters(input);
        assertEquals(expected, actual);
    }

    @Test
    void removeSpecialCharacters_shouldHandleNullInput() {
        assertThrows(NullPointerException.class, () -> formatter.removeSpecialCharacters(null));
    }

    @Test
    void replaceSpaces_shouldReplaceSingleSpaceWithUnderscore() {
        String input = "my file.png";
        String expected = "my_file.png";
        String actual = formatter.replaceSpaces(input);
        assertEquals(expected, actual);
    }

    @Test
    void replaceSpaces_shouldReplaceMultipleSpacesWithOneUnderscore() {
        String input = "my     file   name.png";
        String expected = "my_file_name.png";
        String actual = formatter.replaceSpaces(input);
        assertEquals(expected, actual);
    }

    @Test
    void replaceSpaces_shouldHandleTabsAndNewlines() {
        String input = "my\tfile\nname.png";
        String expected = "my_file_name.png";
        String actual = formatter.replaceSpaces(input);
        assertEquals(expected, actual);
    }

    @Test
    void replaceSpaces_shouldNotModifyTextWithoutWhitespace() {
        String input = "my_file-name.png";
        String actual = formatter.replaceSpaces(input);
        assertEquals(input, actual);
    }

    @Test
    void replaceSpaces_shouldHandleEmptyString() {
        String input = "";
        String actual = formatter.replaceSpaces(input);
        assertEquals("", actual);
    }

    @Test
    void replaceSpaces_shouldHandleNullInput() {
        assertThrows(NullPointerException.class, () -> formatter.replaceSpaces(null));
    }
}