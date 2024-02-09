package ru.practicum.event.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.StatClient;
import ru.practicum.category.model.Category;
import ru.practicum.category.storage.CategoryRepository;
import ru.practicum.event.EventMapper;
import ru.practicum.event.dto.*;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.Location;
import ru.practicum.event.model.QEvent;
import ru.practicum.event.storage.EventRepository;
import ru.practicum.event.storage.LocationRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.storage.UserRepository;
import ru.practicum.util.exception.DateException;
import ru.practicum.util.exception.EventValidationException;
import ru.practicum.util.exception.ResourceNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ru.practicum.util.Util.FORMATTER;

/**
 * Class service for operations with events storage
 */
@Service
public class EventServiceImpl implements EventService {

    private static final int USER_HOURS = 2;
    private static final int ADMIN_HOURS = 1;

    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final StatClient statClient;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, LocationRepository locationRepository,
                            CategoryRepository categoryRepository, UserRepository userRepository,
                            StatClient statClient) {
        this.eventRepository = eventRepository;
        this.locationRepository = locationRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.statClient = statClient;
    }


    /**
     * Add new event
     * with validation of date, category, user
     *
     * @param newEvent to add
     * @param userId   of user
     * @return added event
     */
    @Override
    public EventFullDto createEvent(NewEventDto newEvent, int userId) {
        validateDate(LocalDateTime.parse(newEvent.getEventDate(), FORMATTER), USER_HOURS);

        Location location = locationRepository.save(EventMapper.toLocation(newEvent.getLocation()));

        Event eventToSave = EventMapper.toEvent(newEvent,
                categoryRepository.findById(newEvent.getCategory()).orElseThrow(() ->
                        new ResourceNotFoundException("Category with id=" + newEvent.getCategory() + " was not found.")),
                userRepository.findById(userId).orElseThrow(() ->
                        new ResourceNotFoundException("User with id=" + userId + " was not found.")),
                location
        );

        return EventMapper.toEventFullDto(eventRepository.save(eventToSave));
    }

    /**
     * Update event by id
     * by user
     *
     * @param updateEvent to update
     * @param userId      of user
     * @param eventId     of the event
     * @return updated event
     */
    @Override
    public EventFullDto updateEventByUser(UpdateEventUserRequest updateEvent, int userId, int eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new ResourceNotFoundException("Event with id=" + eventId + " was not found."));

        if (userId != event.getInitiator().getId()) {
            throw new EventValidationException("User and event's initiator do not match.");
        }

        // Validation of state, user, date
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new EventValidationException("Only pending or canceled events can be changed.");
        }
        validateUserId(userId);

        if (updateEvent.getEventDate() != null) {
            validateDate(LocalDateTime.parse(updateEvent.getEventDate(), FORMATTER), USER_HOURS);
        } else {
            validateDate(event.getEventDate(), USER_HOURS);
        }

        // Check & save location
        Location location;
        if (updateEvent.getLocation() != null) {
            location = locationRepository.save(EventMapper.toLocation(updateEvent.getLocation()));
        } else {
            location = locationRepository.save(event.getLocation());
        }

        // Check category
        Category category = validateCategory(updateEvent.getCategory(), event.getCategory().getId());

        Event updatedEvent = eventRepository.save(EventMapper.toEvent(updateEvent, event, category, location));

        EventFullDto result = EventMapper.toEventFullDto(updatedEvent);
        result.setMyRating(getMyRating(userId, eventId));

        return result;
    }

    /**
     * Update event by id
     * by admin
     *
     * @param updateEvent to update
     * @param eventId     of the event
     * @return updated event
     */
    @Override
    public EventFullDto updateEventByAdmin(UpdateEventAdminRequest updateEvent, int eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new ResourceNotFoundException("Event with id=" + eventId + " was not found."));

        // Validation of state
        if (updateEvent.getStateAction() != null) {
            if (updateEvent.getStateAction().equals(AdminStateAction.PUBLISH_EVENT)
                    && (event.getState().equals(EventState.PUBLISHED)
                    || event.getState().equals(EventState.CANCELED))) {
                throw new EventValidationException("Cannot publish the event because it's not in the right state:"
                        + event.getState().name());
            }
            if (updateEvent.getStateAction().equals(AdminStateAction.REJECT_EVENT)
                    && event.getState().equals(EventState.PUBLISHED)) {
                throw new EventValidationException("Cannot reject the event because it's not in the right state:"
                        + event.getState().name());
            }
        }

        if (updateEvent.getEventDate() != null) {
            validateDate(LocalDateTime.parse(updateEvent.getEventDate(), FORMATTER), ADMIN_HOURS);
        } else {
            validateDate(event.getEventDate(), ADMIN_HOURS);
        }

        // Check & save location
        Location location;
        if (updateEvent.getLocation() != null) {
            location = locationRepository.save(EventMapper.toLocation(updateEvent.getLocation()));
        } else {
            location = locationRepository.save(event.getLocation());
        }

        // Check category
        Category category = validateCategory(updateEvent.getCategory(), event.getCategory().getId());

        Event updatedEvent = eventRepository.save(EventMapper.toEvent(updateEvent, event, category, location));

        return EventMapper.toEventFullDto(updatedEvent);
    }

    /**
     * Get event by id
     *
     * @param userId  of user
     * @param eventId of event
     * @return event
     */
    @Override
    public EventFullDto getEventById(int userId, int eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new ResourceNotFoundException("Event with id=" + eventId + " was not found."));

        if (userId != event.getInitiator().getId()) {
            throw new EventValidationException("User and event's initiator do not match.");
        }

        validateUserId(userId);

        EventFullDto result = EventMapper.toEventFullDto(event);
        result.setMyRating(getMyRating(userId, eventId));

        return result;
    }

    /**
     * Get event by id
     * public request
     *
     * @param eventId of event
     * @param uri     of the request
     * @param ip      of a user
     * @return event
     */
    @Override
    public EventFullDto getEventByIdPublic(int eventId, String uri, String ip) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new ResourceNotFoundException("Event with id=" + eventId + " was not found."));

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ResourceNotFoundException("Event should be published, event state=" + event.getState().name());
        }

        statClient.saveHit("ewm-main", uri, ip, LocalDateTime.now().format(FORMATTER));
        String[] uris = {uri};
        String start = LocalDateTime.now().minusYears(10).format(FORMATTER);
        String end = LocalDateTime.now().format(FORMATTER);
        List<LinkedHashMap<String, Object>> listStat = (List<LinkedHashMap<String, Object>>) statClient
                .getStat(start, end, uris, true)
                .getBody();

        event.setViews((Integer) listStat.get(0).get("hits"));

        eventRepository.save(event);

        return EventMapper.toEventFullDto(event);
    }

    /**
     * Get events for current user
     * with pagination
     *
     * @param userId of user
     * @param from   index of the first element
     * @param size   number of elements to return
     * @return list of events
     */
    @Override
    public List<EventShortDto> getEventsByUser(int userId, Integer from, Integer size) {
        validateUserId(userId);
        PageRequest page = PageRequest.of(from / size, size);
        BooleanExpression byUserId = QEvent.event.initiator.id.eq(userId);
        List<Event> eventList = eventRepository.findAll(byUserId, page).getContent();

        List<EventShortDto> result = EventMapper.toEventShortDtoList(eventList);
        result.stream().peek(eventShortDto ->
                eventShortDto.setMyRating(getMyRating(userId, eventShortDto.getId())));

        return result;
    }

    /**
     * Get events by parameters
     * by admin
     * with pagination
     *
     * @param users      list of user's ids
     * @param states     list of states
     * @param categories list of categories
     * @param rangeStart start date
     * @param rangeEnd   end date
     * @param from       index of the first element
     * @param size       number of elements to return
     * @return list of events
     */
    @Override
    public List<EventFullDto> getEventsByAdmin(Integer[] users, String[] states, Integer[] categories,
                                               String rangeStart, String rangeEnd, Integer from, Integer size) {
        BooleanBuilder builder = new BooleanBuilder();

        if (users != null) {
            builder.and(QEvent.event.initiator.id.in(users));
        }
        if (states != null) {
            builder.and(QEvent.event.state.in(
                    Arrays.stream(states)
                            .map(EventState::valueOf)
                            .collect(Collectors.toList())));
        }
        if (categories != null) {
            builder.and(QEvent.event.category.id.in(categories));
        }
        if (rangeStart != null) {
            LocalDateTime startTime = LocalDateTime.parse(rangeStart, FORMATTER);
            builder.and(QEvent.event.eventDate.after(startTime));
        }
        if (rangeEnd != null) {
            LocalDateTime endTime = LocalDateTime.parse(rangeEnd, FORMATTER);
            builder.and(QEvent.event.eventDate.before(endTime));
        }

        PageRequest page = PageRequest.of(from / size, size);
        List<Event> eventList = eventRepository.findAll(builder, page).getContent();
        return EventMapper.toEventFullDto(eventList);
    }

    /**
     * Get events by parameters
     * public request
     * with pagination
     *
     * @param text          to find in event's annotation & description
     * @param categories    list of categories
     * @param paid          true - paid events,
     *                      false - free events
     * @param rangeStart    start date
     * @param rangeEnd      end date
     * @param onlyAvailable true  - events with limit,
     *                      false - all events
     * @param sort          sort type by date/views
     * @param from          index of the first element
     * @param size          number of elements to return
     * @param request       to get uri & ip
     * @return list of events
     */
    @Override
    public List<EventShortDto> getEventsPublic(String text, Integer[] categories, Boolean paid,
                                               String rangeStart, String rangeEnd,
                                               Boolean onlyAvailable, String sort,
                                               Integer from, Integer size,
                                               HttpServletRequest request) {
        statClient.saveHit("ewm-main", request.getRequestURI(), request.getRemoteAddr(),
                LocalDateTime.now().format(FORMATTER));
        BooleanBuilder builder = new BooleanBuilder();
        Sort.Direction sortDirection = Sort.Direction.DESC;

        builder.and(QEvent.event.state.eq(EventState.PUBLISHED));
        if (text != null) {
            BooleanExpression searchByAnnotation = QEvent.event.annotation.containsIgnoreCase(text);
            BooleanExpression searchByDescription = QEvent.event.description.containsIgnoreCase(text);
            builder.and(searchByAnnotation.or(searchByDescription));
        }
        if (categories != null) {
            builder.and(QEvent.event.category.id.in(categories));
        }
        if (paid != null) {
            builder.and(QEvent.event.paid.eq(paid));
        }
        if (rangeStart != null && rangeEnd != null) {
            LocalDateTime startTime = LocalDateTime.parse(rangeStart, FORMATTER);
            LocalDateTime endTime = LocalDateTime.parse(rangeEnd, FORMATTER);
            if (startTime.isAfter(endTime)) {
                throw new DateException("Wrong dates in request.");
            }
            builder.and(QEvent.event.eventDate.after(startTime));
            builder.and(QEvent.event.eventDate.before(endTime));
        } else {
            LocalDateTime startTime = LocalDateTime.now();
            builder.and(QEvent.event.eventDate.after(startTime));
        }
        if (onlyAvailable) {
            builder.and(QEvent.event.participantLimit.goe(QEvent.event.confirmedRequests));
        }
        PageRequest page;
        if (sort != null) {
            if (sort.equals("EVENT_DATE")) {
                sort = "eventDate";
            } else {
                sort = sort.toLowerCase();
            }
            page = PageRequest.of(from / size, size, Sort.by(sortDirection, sort));
        } else {
            page = PageRequest.of(from / size, size);
        }

        List<Event> eventList = eventRepository.findAll(builder, page).getContent();

        return EventMapper.toEventShortDtoList(eventList);
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
     * Validation of time
     *
     * @param time  to validate
     * @param hours to add
     */
    private void validateDate(LocalDateTime time, int hours) {
        if (time.isBefore(LocalDateTime.now().plusHours(hours))) {
            throw new DateException("Error, wrong value of date=" + time.format(FORMATTER));
        }
    }

    /**
     * Validation of category
     *
     * @param updCatId of update category
     * @param oldCatId of old category
     */
    private Category validateCategory(Integer updCatId, int oldCatId) {
        Category category;
        if (updCatId == null) {
            category = categoryRepository.findById(oldCatId).orElseThrow();
        } else {
            category = categoryRepository.findById(updCatId).orElseThrow(() ->
                    new ResourceNotFoundException("Category with id=" + updCatId + " was not found."));
        }
        return category;
    }

    /**
     * Get personal user rating for the event
     *
     * @param userId  of user
     * @param eventId of the event
     * @return personal rating
     */
    private Integer getMyRating(int userId, int eventId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User with id=" + userId + " was not found."));

        AtomicInteger personalRating = new AtomicInteger();
        user.getUserRatings().stream().peek(rating -> {
            if (rating.getEventId() == eventId) {
                personalRating.set(rating.getRating());
            }
        });
        return personalRating.get();
    }
}
