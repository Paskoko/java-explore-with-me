package ru.practicum.util.exception;

/**
 * Custom exception for category validation
 */
public class NotEmptyCategoryException extends RuntimeException {
    public NotEmptyCategoryException(String message) {
        super(message);
    }
}
