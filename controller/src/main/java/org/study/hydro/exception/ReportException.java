package org.study.hydro.exception;

import org.springframework.http.HttpStatus;

/**
 * This class {@link ReportException} is for generating exception report to send to client.
 */
public class ReportException {

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
