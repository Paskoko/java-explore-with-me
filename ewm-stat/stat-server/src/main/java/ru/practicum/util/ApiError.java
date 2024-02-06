package ru.practicum.util;

import lombok.Builder;
import lombok.Data;

/**
 * Class for response errors
 */
@Data
@Builder
public class ApiError {
    private String status;
    private String reason;
    private String message;
    private String timeStamp;
}
