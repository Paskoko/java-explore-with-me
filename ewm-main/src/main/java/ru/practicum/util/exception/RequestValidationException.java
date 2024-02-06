package ru.practicum.util.exception;

/**
 * Custom exception for request validation
 */
public class RequestValidationException extends RuntimeException {
    public RequestValidationException(String message) {
        super(message);
    }
}
