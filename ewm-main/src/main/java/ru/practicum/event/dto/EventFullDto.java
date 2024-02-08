package ru.practicum.event.dto;


import lombok.Builder;
import lombok.Data;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.user.dto.UserShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * DTO class with event's components full
 */
@Data
@Builder
public class EventFullDto {
    private int id;
    @NotBlank
    private String annotation;
    @NotNull
    private CategoryDto category;
    private int confirmedRequests;
    private String createdOn;
    private String description;
    @NotBlank
    private String eventDate;
    @NotNull
    private UserShortDto initiator;
    @NotNull
    private LocationDto location;
    @NotNull
    private Boolean paid;
    private int participantLimit;
    private String publishedOn;
    private boolean requestModeration;
    private EventState state;
    @NotBlank
    private String title;
    private int views;
}
