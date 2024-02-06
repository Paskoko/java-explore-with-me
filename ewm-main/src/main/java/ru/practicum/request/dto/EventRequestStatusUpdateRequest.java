package ru.practicum.request.dto;

import lombok.Data;
import ru.practicum.request.model.RequestStatus;

import java.util.List;

/**
 * DTO class with request's update status components
 */
@Data
public class EventRequestStatusUpdateRequest {
    List<Integer> requestIds;
    RequestStatus status;
}
