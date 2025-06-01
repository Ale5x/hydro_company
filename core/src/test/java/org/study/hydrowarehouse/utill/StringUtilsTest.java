package org.study.hydrowarehouse.utill;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {

    @Test
    public void isBlankOrNullText_shouldReturnTrue_whenInputIsNull() {
        assertTrue(StringUtils.isBlankOrNullText(null));
    }

    @Test
    public void isBlankOrNullText_shouldReturnTrue_whenInputIsEmpty() {
        assertTrue(StringUtils.isBlankOrNullText(""));
    }

    @Test
    public void isBlankOrNullText_shouldReturnTrue_whenInputIsWhitespaceOnly() {
        assertTrue(StringUtils.isBlankOrNullText("   \t\n "));
    }

    @Test
    public void isBlankOrNullText_shouldReturnFalse_whenInputHasText() {
        assertFalse(StringUtils.isBlankOrNullText("Hello"));
    }

    @Test
    public void isBlankOrNullText_shouldReturnFalse_whenInputHasTextAndSpaces() {
        assertFalse(StringUtils.isBlankOrNullText("  text  "));
    }

    @Test
    public void isNullNumericObject_shouldReturnTrue_whenInputIsNull() {
        assertTrue(StringUtils.isNullNumericObject(null));
    }

    @Test
    public void isNullNumericObject_shouldReturnFalse_whenInputIsInteger() {
        assertFalse(StringUtils.isNullNumericObject(42));
    }

    @Test
    public void isNullNumericObject_shouldReturnFalse_whenInputIsDouble() {
        assertFalse(StringUtils.isNullNumericObject(3.14));
    }

    @Test
    public void isNullNumericObject_shouldReturnFalse_whenInputIsFloat() {
        assertFalse(StringUtils.isNullNumericObject(2.71f));
    }

    @Test
    public void isNullNumericObject_shouldReturnFalse_whenInputIsByte() {
        assertFalse(StringUtils.isNullNumericObject((byte) 1));
    }

    @Test
    public void isNullNumericObject_shouldReturnFalse_whenInputIsNumericString() {
        assertFalse(StringUtils.isNullNumericObject("123"));
    }

    @Test
    public void isNullNumericObject_shouldReturnFalse_whenInputIsZero() {
        assertFalse(StringUtils.isNullNumericObject(0));
    }
}