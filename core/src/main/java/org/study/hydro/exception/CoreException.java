package org.study.hydro.exception;

public class CoreException extends RuntimeException {
    public CoreException() {
    }

    public CoreException(String message) {
        super(message);
    }

    public CoreException(String message, Throwable cause) {
        super(message, cause);
    }
}
