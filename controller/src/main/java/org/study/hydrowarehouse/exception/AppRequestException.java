package org.study.hydrowarehouse.exception;

import org.springframework.http.HttpStatus;

/**
 * This class {@link AppRequestException} is responsible for exceptions that occur in the controller.
 * Errors are wrapped for further work.
 *
 * @author Aliaksandr Pishchala
 */
public class AppRequestException extends RuntimeException {

    private HttpStatus status;

    public AppRequestException(HttpStatus status) {
        this.status = status;
    }

    public AppRequestException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
