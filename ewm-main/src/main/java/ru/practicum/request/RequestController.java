package ru.practicum.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.service.RequestService;

import java.util.List;

/**
 * Class controller for requests
 */
@RestController
public class RequestController {

    private final RequestService requestService;

    @Autowired
    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    /**
     * POST request handler to create new request
     *
     * @param userId  of creator
     * @param eventId of event
     * @return created request
     */
    @PostMapping(value = "/users/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createRequest(@PathVariable int userId,
                                                 @RequestParam(value = "eventId") int eventId) {
        return requestService.createRequest(userId, eventId);
    }

    /**
     * PATCH request handler to cancel request by id
     *
     * @param userId    of creator
     * @param requestId of request
     * @return cancelled request
     */
    @PatchMapping(value = "/users/{userId}/requests/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public ParticipationRequestDto cancelRequest(@PathVariable int userId,
                                                 @PathVariable int requestId) {
        return requestService.cancelRequest(userId, requestId);
    }

    /**
     * GET request handler to get user's requests
     *
     * @param userId of owner
     * @return list of requests
     */
    @GetMapping(value = "/users/{userId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getRequests(@PathVariable int userId) {
        return requestService.getRequests(userId);
    }

    /**
     * GET request handler to get user's requests of the event
     *
     * @param userId  of owner
     * @param eventId of event
     * @return list of requests
     */
    @GetMapping(value = "/users/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getRequestsByEvent(@PathVariable int userId,
                                                            @PathVariable int eventId) {
        return requestService.getRequestsByEvent(userId, eventId);
    }

    /**
     * PATCH request handler to change request status
     *
     * @param userId  of owner
     * @param eventId of event
     * @param request to change
     * @return changed request
     */
    @PatchMapping(value = "/users/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult changeRequest(@PathVariable int userId,
                                                        @PathVariable int eventId,
                                                        @RequestBody(required = false) EventRequestStatusUpdateRequest request) {
        return requestService.changeRequest(userId, eventId, request);
    }
}
