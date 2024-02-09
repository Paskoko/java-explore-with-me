package ru.practicum.rating.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.event.EventMapper;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.QEvent;
import ru.practicum.event.storage.EventRepository;
import ru.practicum.rating.model.QRating;
import ru.practicum.rating.model.Rating;
import ru.practicum.rating.storage.RatingRepository;
import ru.practicum.request.model.QRequest;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.request.storage.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.storage.UserRepository;
import ru.practicum.util.exception.RequestValidationException;
import ru.practicum.util.exception.ResourceNotFoundException;

import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class service for operations with ratings storage
 */
@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    @Autowired
    public RatingServiceImpl(RatingRepository ratingRepository, UserRepository userRepository, EventRepository eventRepository, RequestRepository requestRepository) {
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.requestRepository = requestRepository;
    }


    /**
     * Set rating for the event
     *
     * @param userId      of user
     * @param eventId     of the event
     * @param eventRating 1-5
     * @return rated event
     */
    @Override
    public EventFullDto setRating(int userId, int eventId, int eventRating) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User with id=" + userId + " was not found."));
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new ResourceNotFoundException("Event with id=" + eventId + " was not found."));
        if ((eventRating < 1) || (eventRating > 5)) {
            throw new ValidationException("Rating should be from 1 to 5.");
        }

        BooleanExpression byUserId = QRequest.request.requester.id.eq(userId);
        BooleanExpression byEventId = QRequest.request.event.id.eq(eventId);
        BooleanExpression byState = QRequest.request.status.eq(RequestStatus.CONFIRMED);
        List<Request> requests = (List<Request>) requestRepository.findAll(byUserId.and(byEventId).and(byState));
        if (requests.isEmpty()) {
            throw new RequestValidationException("User with id=" + userId + " has not visited this event.");
        }

        Rating newRating = Rating.builder()
                .eventId(event.getId())
                .rating(eventRating)
                .build();

        // Upd user rating list & change rating if user rate event second time -> just update
        List<Rating> userRatings = user.getUserRatings();
        userRatings.add(newRating);
        user.setUserRatings(userRatings);
        userRepository.save(user);
        ratingRepository.save(newRating);


        BooleanExpression ratingByEvent = QRating.rating1.eventId.eq(eventId);
        List<Rating> ratingList = (List<Rating>) ratingRepository.findAll(ratingByEvent);

        // Add rating to the event avg rating
        Double eventAvgRating = event.getRating();
        int numEventRating = ratingList.size();
        if (eventAvgRating != null) {
            eventAvgRating = (eventAvgRating + eventRating) / numEventRating;
        } else {
            eventAvgRating = (double) eventRating;
        }
        event.setRating(eventAvgRating);
        eventRepository.save(event);


        // Add rating to the event owner avg rating
        User owner = userRepository.findById(event.getInitiator().getId()).orElseThrow();
        Double ownerAvgRating = owner.getRating();
        // Owner rating = avg rating of all his events
        int numOwnerRatings = countOwnerRatedEvents(owner);
        if (ownerAvgRating != null) {
            ownerAvgRating = (ownerAvgRating + eventAvgRating) / numOwnerRatings;
        } else {
            ownerAvgRating = eventAvgRating;
        }
        owner.setRating(ownerAvgRating);
        userRepository.save(owner);

        EventFullDto result = EventMapper.toEventFullDto(event);
        result.setMyRating(eventRating);

        return result;
    }


    /**
     * Delete rate for the event
     *
     * @param userId  of user
     * @param eventId of the event
     * @return event without user's rating
     */
    @Override
    public EventFullDto deleteRating(int userId, int eventId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User with id=" + userId + " was not found."));
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new ResourceNotFoundException("Event with id=" + eventId + " was not found."));

        // Find and delete rating from user's list and database

        List<Rating> userRatings = user.getUserRatings();
        Rating deletedRating = userRatings.stream()
                .filter(r -> r.getEventId() == eventId)
                .findFirst().get();
        ratingRepository.delete(deletedRating);
        userRatings.remove(deletedRating);
        user.setUserRatings(userRatings);
        userRepository.save(user);


        BooleanExpression ratingByEvent = QRating.rating1.eventId.eq(eventId);
        List<Rating> ratingList = (List<Rating>) ratingRepository.findAll(ratingByEvent);

        // Delete rating from the event avg rating
        Double eventAvgRating = event.getRating();
        eventAvgRating = ratingList.size() * eventAvgRating - deletedRating.getRating();
        event.setRating(eventAvgRating);
        eventRepository.save(event);

        // Delete rating from the event owner avg rating
        User owner = event.getInitiator();
        Double ownerAvgRating = owner.getRating();
        int numOwnerRatings = countOwnerRatedEvents(owner);
        ownerAvgRating = numOwnerRatings * ownerAvgRating - deletedRating.getRating();
        owner.setRating(ownerAvgRating);
        userRepository.save(owner);

        EventFullDto result = EventMapper.toEventFullDto(event);
        result.setMyRating(null);

        return result;
    }

    /**
     * Get user's rated events
     *
     * @param userId of user
     * @return list of events
     */
    @Override
    public List<EventShortDto> getUserRatingByEvent(int userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User with id=" + userId + " was not found."));

        return user.getUserRatings().stream()
                .map(rating -> {
                    int eventId = rating.getEventId();
                    Event event = eventRepository.findById(eventId).orElseThrow(() ->
                            new ResourceNotFoundException("Event with id=" + eventId + " was not found."));
                    EventShortDto eventShortDto = EventMapper.toEventShortDto(event);
                    eventShortDto.setMyRating(rating.getRating());
                    return eventShortDto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Count rated events for user
     *
     * @param owner user
     * @return number of rated events
     */
    private int countOwnerRatedEvents(User owner) {
        BooleanExpression byUserId = QEvent.event.initiator.id.eq(owner.getId());
        BooleanExpression byRate = QEvent.event.rating.isNotNull();
        List<Event> eventList = (List<Event>) eventRepository.findAll(byUserId.and(byRate));
        return eventList.size();
    }
}
