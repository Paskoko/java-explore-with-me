package ru.practicum.request.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.event.dto.EventState;
import ru.practicum.event.model.Event;
import ru.practicum.event.storage.EventRepository;
import ru.practicum.request.RequestMapper;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.model.QRequest;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.request.storage.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.storage.UserRepository;
import ru.practicum.util.exception.RequestValidationException;
import ru.practicum.util.exception.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Class service for operations with requests storage
 */
@Service
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository,
                              UserRepository userRepository, EventRepository eventRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    /**
     * Add new request
     *
     * @param userId  of creator
     * @param eventId of event
     * @return added request
     */
    @Override
    public ParticipationRequestDto createRequest(int userId, int eventId) {
        BooleanExpression byUserId = QRequest.request.requester.id.eq(userId);
        BooleanExpression byEventId = QRequest.request.event.id.eq(eventId);
        List<Request> requests = (List<Request>) requestRepository.findAll(byUserId.and(byEventId));
        if (!requests.isEmpty()) {
            throw new RequestValidationException("Repeated request.");
        }
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User with id=" + userId + " was not found."));
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new ResourceNotFoundException("Event with id=" + eventId + " was not found."));

        if (event.getInitiator().getId() == userId) {
            throw new RequestValidationException("Initiator cannot participate in own event.");
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new RequestValidationException("Forbidden participate in not published event.");
        }

        Request newRequest = Request.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(user)
                .status(RequestStatus.PENDING)
                .build();

        if ((event.getParticipantLimit() != 0) && (event.getConfirmedRequests() == event.getParticipantLimit())) {
            throw new RequestValidationException("Event participation limit has been reached.");
        }
        if (event.getParticipantLimit() == 0) {
            newRequest.setStatus(RequestStatus.CONFIRMED);
        }
        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        eventRepository.save(event);

        return RequestMapper.toParticipationRequestDto(requestRepository.save(newRequest));
    }

    /**
     * Cancel request
     *
     * @param userId    of creator
     * @param requestId of request
     * @return cancelled request
     */
    @Override
    public ParticipationRequestDto cancelRequest(int userId, int requestId) {
        validateUserId(userId);

        Request request = requestRepository.findById(requestId).orElseThrow(() ->
                new ResourceNotFoundException("Request with id=" + requestId + " was not found."));

        request.setStatus(RequestStatus.CANCELED);
        Event event = request.getEvent();
        event.setConfirmedRequests(event.getConfirmedRequests() - 1);
        eventRepository.save(event);

        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    /**
     * Get user's requests
     *
     * @param userId of owner
     * @return list of requests
     */
    @Override
    public List<ParticipationRequestDto> getRequests(int userId) {
        validateUserId(userId);

        BooleanExpression byUserId = QRequest.request.requester.id.eq(userId);
        List<Request> requests = (List<Request>) requestRepository.findAll(byUserId);

        return RequestMapper.toParticipationRequestDtoList(requests);
    }

    /**
     * Get requests to user's event
     *
     * @param userId  of owner
     * @param eventId of event
     * @return list of requests
     */
    @Override
    public List<ParticipationRequestDto> getRequestsByEvent(int userId, int eventId) {
        validateUserId(userId);
        validateEventId(eventId);

        BooleanExpression byUserId = QRequest.request.event.initiator.id.eq(userId);
        BooleanExpression byEventId = QRequest.request.event.id.eq(eventId);

        List<Request> requests = (List<Request>) requestRepository.findAll(byUserId.and(byEventId));

        return RequestMapper.toParticipationRequestDtoList(requests);
    }

    /**
     * Change request's status for user's event
     *
     * @param userId        of owner
     * @param eventId       of event
     * @param updateRequest to change
     * @return changed request
     */
    @Override
    public EventRequestStatusUpdateResult changeRequest(int userId, int eventId,
                                                        EventRequestStatusUpdateRequest updateRequest) {
        if (updateRequest == null) {
            throw new RequestValidationException("No requests to update.");
        }

        validateUserId(userId);

        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new ResourceNotFoundException("Event with id=" + eventId + " was not found."));

        BooleanExpression byId = QRequest.request.id.in(updateRequest.getRequestIds());
        List<Request> requestList = (List<Request>) requestRepository.findAll(byId);
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();

        int limit = event.getParticipantLimit();
        AtomicInteger confirmed = new AtomicInteger(event.getConfirmedRequests());


        if ((limit != 0) && (confirmed.get() == limit)) {
            throw new RequestValidationException("Event participation limit has been reached.");
        }

        List<Request> confirmedRequests = new ArrayList<>();
        List<Request> cancelledRequests = new ArrayList<>();
        requestList.stream().peek(request -> {
            if (!request.getStatus().equals(RequestStatus.PENDING)) {
                throw new RequestValidationException("Forbidden to change state in not pending requests.");
            }
            if (updateRequest.getStatus().equals(RequestStatus.CONFIRMED)) {
                if (confirmed.get() <= limit) {
                    request.setStatus(RequestStatus.CONFIRMED);
                    confirmedRequests.add(request);
                    confirmed.getAndIncrement();
                } else {
                    request.setStatus(RequestStatus.CANCELED);
                    cancelledRequests.add(request);
                }
            }
            if (updateRequest.getStatus().equals(RequestStatus.REJECTED)) {
                request.setStatus(RequestStatus.REJECTED);
                cancelledRequests.add(request);
            }
        }).collect(Collectors.toList());


        result.setConfirmedRequests(RequestMapper.toParticipationRequestDtoList(confirmedRequests));
        result.setRejectedRequests(RequestMapper.toParticipationRequestDtoList(cancelledRequests));

        return result;
    }

    /**
     * Validation of user
     *
     * @param userId of user
     */
    private void validateUserId(int userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new ResourceNotFoundException("User with id=" + userId + " was not found.");
        }
    }

    /**
     * Validation of event
     *
     * @param eventId of event
     */
    private void validateEventId(int eventId) {
        if (eventRepository.findById(eventId).isEmpty()) {
            throw new ResourceNotFoundException("Event with id=" + eventId + " was not found.");
        }
    }
}
