package org.study.hydrowarehouse.utill.filestorage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CyrillicFileNameTransliteratorTest {

    private CyrillicFileNameTransliterator transliterator;

    @BeforeEach
    void setUp() {
        transliterator = new CyrillicFileNameTransliterator();
    }

    @Test
    void transliterate_shouldHandleAllCyrillicCharacters() {
        String input = "ПриветМир";
        String expected = "PrivetMir";

        String result = transliterator.transliterate(input);

        assertEquals(expected, result);
    }

    @Test
    void transliterate_shouldHandleLowerAndUpperCyrillic() {
        String input = "ЮлияЯн";
        String expected = "YuliyaYan";

        String result = transliterator.transliterate(input);

        assertEquals(expected, result);
    }

    @Test
    void transliterate_shouldIgnoreLatinCharacters() {
        String input = "testФайл.txt";
        String expected = "testFayl.txt";

        String result = transliterator.transliterate(input);

        assertEquals(expected, result);
    }

    @Test
    void transliterate_shouldPreserveNumbersAndSymbols() {
        String input = "файл_2024#v1!.jpg";
        String expected = "fayl_2024#v1!.jpg";

        String result = transliterator.transliterate(input);

        assertEquals(expected, result);
    }

    @Test
    void transliterate_shouldReturnEmptyString_forEmptyInput() {
        String result = transliterator.transliterate("");

        assertEquals("", result);
    }

    @Test
    void transliterate_shouldReturnSameString_ifNoCyrillic() {
        String input = "MyFile-2024.jpg";

        String result = transliterator.transliterate(input);

        assertEquals(input, result);
    }

    @Test
    void transliterate_shouldRemoveSoftAndHardSigns() {
        String input = "объектмоль";
        String expected = "obektmol";

        String result = transliterator.transliterate(input);

        assertEquals(expected, result);
    }

    @Test
    void transliterate_shouldHandleMixedSpecialCharactersAndCyrillic() {
        String input = "пр!о@ек#т$.2024";
        String expected = "pr!o@ek#t$.2024";

        String result = transliterator.transliterate(input);

        assertEquals(expected, result);
    }
}