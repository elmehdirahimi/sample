package com.renault.renault.exception;


public class BusinessConstraintViolationException extends RuntimeException {
    public BusinessConstraintViolationException(String message) {
        super(message);
    }

    public BusinessConstraintViolationException(String message, Throwable cause) {
        super(message, cause);
    }
}
