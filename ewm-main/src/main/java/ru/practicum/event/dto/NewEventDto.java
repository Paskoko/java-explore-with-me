package ru.practicum.event.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * * DTO class with event's components for new requests
 */
@Data
@Builder
public class NewEventDto {
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;
    @NotNull
    private int category;
    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;
    @NotBlank
    private String eventDate;
    @NotNull
    private LocationDto location;
    private Boolean paid;
    @PositiveOrZero
    private Integer participantLimit;
    private Boolean requestModeration;
    @NotBlank
    @Size(min = 3, max = 120)
    private String title;
}
