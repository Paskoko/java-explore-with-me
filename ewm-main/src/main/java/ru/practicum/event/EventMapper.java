package ru.practicum.event;

import ru.practicum.category.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.event.dto.*;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.Location;
import ru.practicum.user.UserMapper;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.util.Util.FORMATTER;

/**
 * Class mapper for event & location object
 */
public class EventMapper {

    /**
     * Transform event to eventShortDto object
     *
     * @param event to transform
     * @return eventShortDto object
     */
    public static EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate().format(FORMATTER))
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    /**
     * Transform newEventDto to event object
     *
     * @param newEventDto to transform
     * @param category    category object
     * @param initiator   user object
     * @param location    location object
     * @return event object
     */
    public static Event toEvent(NewEventDto newEventDto, Category category, User initiator, Location location) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .confirmedRequests(0)
                .createdOn(LocalDateTime.now())
                .description(newEventDto.getDescription())
                .eventDate(LocalDateTime.parse(newEventDto.getEventDate(), FORMATTER))
                .initiator(initiator)
                .location(location)
                .paid(newEventDto.getPaid() != null && newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit() == null
                        ? 0 : newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration() == null ||
                        newEventDto.getRequestModeration())
                .state(EventState.PENDING)
                .title(newEventDto.getTitle())
                .views(0)
                .build();
    }

    /**
     * Transform updateEventUserRequest to event object
     * with comparing with oldEvent
     *
     * @param updateEvent to transform
     * @param oldEvent    event from database
     * @param category    category object
     * @param location    location object
     * @return event object
     */
    public static Event toEvent(UpdateEventUserRequest updateEvent, Event oldEvent,
                                Category category, Location location) {
        return Event.builder()
                .id(oldEvent.getId())
                .annotation(updateEvent.getAnnotation() == null
                        ? oldEvent.getAnnotation() : updateEvent.getAnnotation())
                .category(category)
                .confirmedRequests(oldEvent.getConfirmedRequests())
                .createdOn(oldEvent.getCreatedOn())
                .description(updateEvent.getDescription() == null
                        ? oldEvent.getDescription() : updateEvent.getDescription())
                .eventDate(updateEvent.getEventDate() == null
                        ? oldEvent.getEventDate() : LocalDateTime.parse(updateEvent.getEventDate(), FORMATTER))
                .initiator(oldEvent.getInitiator())
                .location(location)
                .paid(updateEvent.getPaid() == null
                        ? oldEvent.getPaid() : updateEvent.getPaid())
                .participantLimit(updateEvent.getParticipantLimit() == null
                        ? oldEvent.getParticipantLimit() : updateEvent.getParticipantLimit())
                .publishedOn(oldEvent.getPublishedOn())
                .requestModeration(updateEvent.getRequestModeration() == null
                        ? oldEvent.isRequestModeration() : updateEvent.getRequestModeration())
                .state(updateEvent.getStateAction() == null
                        ? oldEvent.getState() : toEventState(updateEvent.getStateAction()))
                .title(updateEvent.getTitle() == null
                        ? oldEvent.getTitle() : updateEvent.getTitle())
                .views(oldEvent.getViews())
                .build();
    }

    /**
     * Transform updateEventAdminRequest to event object
     * with comparing with oldEvent
     *
     * @param updateEvent to transform
     * @param oldEvent    event from database
     * @param category    category object
     * @param location    location object
     * @return event object
     */
    public static Event toEvent(UpdateEventAdminRequest updateEvent, Event oldEvent,
                                Category category, Location location) {
        return Event.builder()
                .id(oldEvent.getId())
                .annotation(updateEvent.getAnnotation() == null
                        ? oldEvent.getAnnotation() : updateEvent.getAnnotation())
                .category(category)
                .confirmedRequests(oldEvent.getConfirmedRequests())
                .createdOn(oldEvent.getCreatedOn())
                .description(updateEvent.getDescription() == null
                        ? oldEvent.getDescription() : updateEvent.getDescription())
                .eventDate(updateEvent.getEventDate() == null
                        ? oldEvent.getEventDate() : LocalDateTime.parse(updateEvent.getEventDate(), FORMATTER))
                .initiator(oldEvent.getInitiator())
                .location(location)
                .paid(updateEvent.getPaid() == null
                        ? oldEvent.getPaid() : updateEvent.getPaid())
                .participantLimit(updateEvent.getParticipantLimit() == null
                        ? oldEvent.getParticipantLimit() : updateEvent.getParticipantLimit())
                .publishedOn(oldEvent.getPublishedOn())
                .requestModeration(updateEvent.getRequestModeration() == null
                        ? oldEvent.isRequestModeration() : updateEvent.getRequestModeration())
                .state(updateEvent.getStateAction() == null
                        ? oldEvent.getState() : toEventState(updateEvent.getStateAction()))
                .title(updateEvent.getTitle() == null
                        ? oldEvent.getTitle() : updateEvent.getTitle())
                .views(oldEvent.getViews())
                .build();
    }

    /**
     * Transform event to eventFullDto object
     *
     * @param event to transform
     * @return eventFullDto object
     */
    public static EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn().format(FORMATTER))
                .description(event.getDescription())
                .eventDate(event.getEventDate().format(FORMATTER))
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .location(toLocationDto(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn() == null
                        ? null : event.getPublishedOn().format(FORMATTER))
                .requestModeration(event.isRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    /**
     * Transform locationDto to location object
     *
     * @param locationDto to transform
     * @return location object
     */
    public static Location toLocation(LocationDto locationDto) {
        return Location.builder()
                .lat(locationDto.getLat())
                .lon(locationDto.getLon())
                .build();
    }

    /**
     * Transform location to locationDto object
     *
     * @param location to transform
     * @return locationDto object
     */
    public static LocationDto toLocationDto(Location location) {
        return LocationDto.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }

    /**
     * Transform list of events to list of eventShortDto objects
     *
     * @param eventList to transform
     * @return list of eventShortDto objects
     */
    public static List<EventShortDto> toEventShortDtoList(List<Event> eventList) {
        return eventList.stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
    }

    /**
     * Transform list of events to list of eventFullDto objects
     *
     * @param eventList to transform
     * @return list of eventFullDto objects
     */
    public static List<EventFullDto> toEventFullDto(List<Event> eventList) {
        return eventList.stream().map(EventMapper::toEventFullDto).collect(Collectors.toList());
    }

    /**
     * Transform userStateAction to eventState object
     *
     * @param userStateAction to transform
     * @return eventsState object
     */
    private static EventState toEventState(UserStateAction userStateAction) {
        if (userStateAction == UserStateAction.SEND_TO_REVIEW) {
            return EventState.PENDING;
        }
        if (userStateAction == UserStateAction.CANCEL_REVIEW) {
            return EventState.CANCELED;
        }
        return null;
    }

    /**
     * Transform adminStateAction to eventState object
     *
     * @param adminStateAction to transform
     * @return eventsState object
     */
    private static EventState toEventState(AdminStateAction adminStateAction) {
        if (adminStateAction == AdminStateAction.PUBLISH_EVENT) {
            return EventState.PUBLISHED;
        }
        if (adminStateAction == AdminStateAction.REJECT_EVENT) {
            return EventState.CANCELED;
        }
        return null;
    }
}
