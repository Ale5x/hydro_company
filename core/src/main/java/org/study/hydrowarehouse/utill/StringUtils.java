package org.study.hydrowarehouse.utill;

/**
 * Utility class for String-related helper methods.
 * <p>
 * Provides static methods for common string checks such as  null or blank detection and numeric object null checks.
 * </p>
 *
 * @author Aliaksandr Pishchala
 */
public class StringUtils {

    /**
     * Checks if a string is null, empty, or contains only whitespace characters.
     *
     * @param str the input string to check
     * @return true if the string is null, empty, or whitespace only
     */
    public static boolean isBlankOrNullText(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Checks whether the provided numeric object value is {@code null}.
     * <p>
     * This method is intended to be used when a numeric value is passed as a {@link String},
     * which could represent types such as {@link Integer}, {@link Double}, {@link Float}, or {@link Byte}
     * in their object form. If the value is {@code null}, this method returns {@code true}.
     *
     * @param value the object representation of a numeric value (e.g., "123", "3.14"), or {@code null}
     * @return {@code true} if the input is {@code null}; {@code false} otherwise
     */
    public static boolean isNullNumericObject(Object value) {
        return value == null;
    }
}
