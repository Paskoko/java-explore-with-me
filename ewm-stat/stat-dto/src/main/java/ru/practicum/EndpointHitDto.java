package ru.practicum;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * DTO class for event's endpoints hits
 * with IP of user
 */
@Data
@Builder
public class EndpointHitDto {
    @NotBlank
    private String app;
    @NotBlank
    private String uri;
    @NotBlank
    private String ip;
    @NotBlank
    private String timeStamp;
}
