package org.study.hydrowarehouse.exception;

import org.springframework.http.HttpStatus;

/**
 * This class {@link ReportException} is for generating exception report to send to client.
 */
public class ReportException extends RuntimeException {

    private HttpStatus status;
    private String message;

    public ReportException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public ReportException(HttpStatus status) {
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
