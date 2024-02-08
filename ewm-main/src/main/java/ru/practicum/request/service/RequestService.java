package ru.practicum.request.service;

import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;

import java.util.List;

/**
 * Interface for request service
 */
public interface RequestService {

    /**
     * Add new request
     *
     * @param userId  of creator
     * @param eventId of event
     * @return added request
     */
    ParticipationRequestDto createRequest(int userId, int eventId);

    /**
     * Cancel request
     *
     * @param userId    of creator
     * @param requestId of request
     * @return cancelled request
     */
    ParticipationRequestDto cancelRequest(int userId, int requestId);

    /**
     * Get user's requests
     *
     * @param userId of owner
     * @return list of requests
     */
    List<ParticipationRequestDto> getRequests(int userId);

    /**
     * Get requests to user's event
     *
     * @param userId  of owner
     * @param eventId of event
     * @return list of requests
     */
    List<ParticipationRequestDto> getRequestsByEvent(int userId, int eventId);

    /**
     * Change request's status for user's event
     *
     * @param userId        of owner
     * @param eventId       of event
     * @param updateRequest to change
     * @return changed request
     */
    EventRequestStatusUpdateResult changeRequest(int userId, int eventId, EventRequestStatusUpdateRequest updateRequest);
}
