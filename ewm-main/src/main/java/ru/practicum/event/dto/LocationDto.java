package ru.practicum.event.dto;

import lombok.Builder;
import lombok.Data;

/**
 * DTO class with location's components
 */
@Data
@Builder
public class LocationDto {
    private float lat;
    private float lon;
}
