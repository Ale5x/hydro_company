package org.study.hydro.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * This class {@link AppExceptionHandler} catches exceptions that occur in the application and sends a response depending on the type of exception.
 *
 * @author Aliaksandr Pishchala
 */
@ControllerAdvice
public class AppExceptionHandler {

    private static final String CODE_500_MESSAGE = "Server doesn't work...";

    /**
     * This method catches CoreException from the core and sends the response with the report and the status.
     * @param coreEx is the CoreException from the core.
     * @return The report with some information like status.
     */
    @ResponseBody
    @ExceptionHandler(value = CoreException.class)
    public ResponseEntity<Object> internalError(CoreException coreEx) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ReportException reportEx = new ReportException(status);
        return new ResponseEntity<>(reportEx, status);
    }

    /**
     * This method catches AppRequestException from controllers and sends the response with the report and the status.
     * @param appEx is the AppRequestException from the controller.
     * @return The report with some information like the status and the message.
     */
    @ResponseBody
    @ExceptionHandler(value = AppRequestException.class)
    public ResponseEntity<Object> externalError(AppRequestException appEx) {
        ReportException reportEx = new ReportException(appEx.getStatus(), appEx.getMessage());
        return new ResponseEntity<>(reportEx, appEx.getStatus());
    }

    /**
     * This method catches Exception from the application which may arise in the application which cannot be foreseen.
     * @param ex is the Exception from the application.
     * @return The report with some information like the status and the message.
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> internalError(Exception ex) {
        ReportException reportEx = new ReportException(HttpStatus.INTERNAL_SERVER_ERROR, CODE_500_MESSAGE);
        return new ResponseEntity<>(reportEx, reportEx.getStatus());
    }
}
