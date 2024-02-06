package ru.practicum.event.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

/**
 * * DTO class with event's components for admin update requests
 */
@Data
@Builder
public class UpdateEventAdminRequest {
    @Size(min = 20, max = 2000)
    private String annotation;
    private Integer category;
    @Size(min = 20, max = 7000)
    private String description;
    private String eventDate;
    private LocationDto location;
    private Boolean paid;
    @Positive
    private Integer participantLimit;
    private Boolean requestModeration;
    private AdminStateAction stateAction;
    @Size(min = 3, max = 120)
    private String title;
}
