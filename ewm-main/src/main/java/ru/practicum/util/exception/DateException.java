package ru.practicum.util.exception;

/**
 * Custom exception for date
 */
public class DateException extends RuntimeException {
    public DateException(String message) {
        super(message);
    }
}
