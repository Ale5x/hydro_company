package org.study.hydro.utill;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.study.hydro.exception.AppRequestException;


@Component
public class ValidatorParam {

    private static final String REGEX_NUMBER = "^\\d+$";

    public static void isNumber(String number) {
        if (!number.matches(REGEX_NUMBER)) {
            new AppRequestException(HttpStatus.BAD_REQUEST);
        }
    }

    public static void validPage(String page) {
        if (!page.matches(REGEX_NUMBER) && Integer.parseInt(page) < 1) {
            new AppRequestException(HttpStatus.BAD_REQUEST);
        }
    }
}
