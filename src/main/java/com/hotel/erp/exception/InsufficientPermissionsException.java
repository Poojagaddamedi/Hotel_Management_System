package com.hotel.erp.exception;

/**
 * Exception thrown when a user doesn't have sufficient permissions to perform
 * an operation
 */
public class InsufficientPermissionsException extends RuntimeException {

    public InsufficientPermissionsException(String message) {
        super(message);
    }

    public InsufficientPermissionsException(String message, Throwable cause) {
        super(message, cause);
    }
}
