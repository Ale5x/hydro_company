package org.study.hydro.exception;

import org.springframework.http.HttpStatus;

public class AppRequestException extends RuntimeException {

    private HttpStatus status;

    public AppRequestException(HttpStatus status) {
        this.status = status;
    }

    public AppRequestException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
