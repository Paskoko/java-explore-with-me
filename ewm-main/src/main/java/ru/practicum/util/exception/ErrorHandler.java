package ru.practicum.util.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.util.exception.model.ApiError;

import javax.validation.ValidationException;
import java.time.LocalDateTime;

import static ru.practicum.util.Util.FORMATTER;

/**
 * Class handler for exceptions
 */
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ApiError handleWrongRequestException(final ValidationException e) {
        return ApiError.builder()
                .status("BAD_REQUEST")
                .reason("Incorrectly made request.")
                .message(e.getMessage())
                .timeStamp((LocalDateTime.now().format(FORMATTER)))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ApiError handleWrongRequestException(final MethodArgumentNotValidException e) {
        return ApiError.builder()
                .status("BAD_REQUEST")
                .reason("Incorrectly made request.")
                .message(e.getMessage())
                .timeStamp((LocalDateTime.now().format(FORMATTER)))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public ApiError handleConflictException(final ConstraintViolationException e) {
        return ApiError.builder()
                .status("CONFLICT")
                .reason("Integrity constraint has been violated.")
                .message(e.getMessage())
                .timeStamp((LocalDateTime.now().format(FORMATTER)))
                .build();
    }


    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public ApiError handleCategoryException(final NotEmptyCategoryException e) {
        return ApiError.builder()
                .status("CONFLICT")
                .reason("For the requested operation the conditions are not met.")
                .message(e.getMessage())
                .timeStamp((LocalDateTime.now().format(FORMATTER)))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ApiError handleResourceNotFoundException(final ResourceNotFoundException e) {
        return ApiError.builder()
                .status("NOT_FOUND")
                .reason("The required object was not found.")
                .message(e.getMessage())
                .timeStamp((LocalDateTime.now().format(FORMATTER)))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public ApiError handleEventValidationException(final EventValidationException e) {
        return ApiError.builder()
                .status("CONFLICT")
                .reason("For the requested operation the conditions are not met.")
                .message(e.getMessage())
                .timeStamp((LocalDateTime.now().format(FORMATTER)))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public ApiError handleRequestValidationException(final RequestValidationException e) {
        return ApiError.builder()
                .status("CONFLICT")
                .reason("For the requested operation the conditions are not met.")
                .message(e.getMessage())
                .timeStamp((LocalDateTime.now().format(FORMATTER)))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ApiError handleDateException(final DateException e) {
        return ApiError.builder()
                .status("BAD_REQUEST")
                .reason("For the requested operation the conditions are not met.")
                .message(e.getMessage())
                .timeStamp((LocalDateTime.now().format(FORMATTER)))
                .build();
    }
}
