package ru.practicum.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
}
