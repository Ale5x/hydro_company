package org.study.hydrowarehouse.utill;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.study.hydrowarehouse.exception.AppRequestException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatCode;

class ValidatorParamTest {

    @Test
    void isNumber() {
        assertThatCode(() -> ValidatorParam.isNumber("123"))
                .doesNotThrowAnyException();
    }

    @Test
    void isNumberThrowWithNegativeNumber() {
        assertThatThrownBy(() -> ValidatorParam.isNumber("-1"))
                .isInstanceOf(AppRequestException.class)
                .extracting("status")
                .isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void isNumberThrowWithText() {
        assertThatThrownBy(() -> ValidatorParam.isNumber("abc"))
                .isInstanceOf(AppRequestException.class);
    }

    @Test
    void isNumberThrowWithDecimalNumber() {
        assertThatThrownBy(() -> ValidatorParam.isNumber("12.5"))
                .isInstanceOf(AppRequestException.class);
    }

    @Test
    void validPage() {
        assertThatCode(() -> ValidatorParam.validPage("5"))
                .doesNotThrowAnyException();
    }

    @Test
    void validPageThrowWithZero() {
        assertThatThrownBy(() -> ValidatorParam.validPage("0"))
                .isInstanceOf(AppRequestException.class);
    }

    @Test
    void validPageThrowWithNegativeNumber() {
        assertThatThrownBy(() -> ValidatorParam.validPage("-3"))
                .isInstanceOf(AppRequestException.class);
    }

    @Test
    void validPageThrowWithText() {
        assertThatThrownBy(() -> ValidatorParam.validPage("page"))
                .isInstanceOf(AppRequestException.class);
    }
}