package org.study.hydrowarehouse.utill;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.study.hydrowarehouse.exception.AppRequestException;

/**
 * Utility component for validating request parameters. Provides methods to check if a string is a valid positive number
 * and to validate specific pagination parameters such as page number.
 * Throws {@link AppRequestException} with {@link HttpStatus#BAD_REQUEST} if validation fails.
 *
 * @author Aliaksandr Pishchala
 */
@Component
public class ValidatorParam {

    private static final String REGEX_NUMBER = "^\\d+$";

    /**
     * Validates that the given string represents a positive number (non-negative integer).
     *
     * @param number the string to validate
     * @throws AppRequestException if the string does not match a positive number format
     */
    public static void isNumber(String number) {
        if (!number.matches(REGEX_NUMBER)) {
            throw new AppRequestException(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Validates that the page number is a positive integer (≥ 1).
     *
     * @param page the string representing the page number
     * @throws AppRequestException if the value is not a valid positive integer ≥ 1
     */
    public static void validPage(String page) {
        if (!page.matches(REGEX_NUMBER) || Integer.parseInt(page) < 1) {
            throw new AppRequestException(HttpStatus.BAD_REQUEST);
        }
    }
}
