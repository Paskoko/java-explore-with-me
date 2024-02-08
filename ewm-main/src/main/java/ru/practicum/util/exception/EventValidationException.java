package ru.practicum.util.exception;

/**
 * Custom exception for event validation
 */
public class EventValidationException extends RuntimeException {
    public EventValidationException(String message) {
        super(message);
    }
}
