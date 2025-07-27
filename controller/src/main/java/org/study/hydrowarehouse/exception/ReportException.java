package org.study.hydrowarehouse.exception;

import org.springframework.http.HttpStatus;

/**
 * This class {@link ReportException} is for generating exception report to send to client.
 */
/**
 * The {@code ReportException} class represents a simplified exception report
 * that contains the HTTP status and an optional error message.
 * <p>
 * It is typically used in exception handlers to send a consistent error response
 * to the client, without exposing internal stack traces or sensitive information.
 * </p>
 *
 * @author Aliaksandr Pishchala
 */
public class ReportException {

    /**
     * The HTTP status of the error (e.g. 404 NOT FOUND, 500 INTERNAL SERVER ERROR).
     */
    private HttpStatus status;

    /**
     * The error message associated with the exception.
     */
    private String message;

    /**
     * Constructs a new {@code ReportException} with both status and message.
     *
     * @param status  the HTTP status of the error
     * @param message the error message to be returned
     */
    public ReportException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    /**
     * Constructs a new {@code ReportException} with only status.
     * The message can be set later.
     *
     * @param status the HTTP status of the error
     */
    public ReportException(HttpStatus status) {
        this.status = status;
    }

    /**
     * Returns the HTTP status of the error.
     *
     * @return the HTTP status
     */
    public HttpStatus getStatus() {
        return status;
    }

    /**
     * Sets the HTTP status of the error.
     *
     * @param status the HTTP status to set
     */
    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    /**
     * Returns the error message.
     *
     * @return the error message, or {@code null} if not set
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the error message.
     *
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
