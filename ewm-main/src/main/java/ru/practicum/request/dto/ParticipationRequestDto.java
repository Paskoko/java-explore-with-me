package ru.practicum.request.dto;

import lombok.Builder;
import lombok.Data;


/**
 * DTO class with request's components
 */
@Data
@Builder
public class ParticipationRequestDto {
    private int id;
    private String created;
    private Integer event;
    private Integer requester;
    private String status;
}
