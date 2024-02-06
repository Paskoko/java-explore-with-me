package ru.practicum.request.dto;

import lombok.Data;

import java.util.List;

/**
 * DTO class with request's update status result components
 */
@Data
public class EventRequestStatusUpdateResult {
    List<ParticipationRequestDto> confirmedRequests;
    List<ParticipationRequestDto> rejectedRequests;
}
