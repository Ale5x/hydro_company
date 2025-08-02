package org.study.hydrowarehouse.exception;

/**
 * This class {@link CoreException} is responsible for exceptions that occur in the core.
 * Errors are wrapped for further work.
 *
 * @see RuntimeException
 *
 * @author Aliaksandr Pishchala
 */
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
