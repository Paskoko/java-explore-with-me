package ru.practicum.event.service;


import ru.practicum.event.dto.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Interface for event service
 */
public interface EventService {

    /**
     * Add new event
     *
     * @param newEvent to add
     * @param userId   of user
     * @return added event
     */
    EventFullDto createEvent(NewEventDto newEvent, int userId);

    /**
     * Update event by id
     * by user
     *
     * @param updateEvent to update
     * @param userId      of user
     * @param eventId     of the event
     * @return updated event
     */
    EventFullDto updateEventByUser(UpdateEventUserRequest updateEvent, int userId, int eventId);

    /**
     * Update event by id
     * by admin
     *
     * @param updateEvent to update
     * @param eventId     of the event
     * @return updated event
     */
    EventFullDto updateEventByAdmin(UpdateEventAdminRequest updateEvent, int eventId);

    /**
     * Get event by id
     *
     * @param userId  of user
     * @param eventId of event
     * @return event
     */
    EventFullDto getEventById(int userId, int eventId);

    /**
     * Get event by id
     * public request
     *
     * @param eventId of event
     * @param uri     of the request
     * @param ip      of a user
     * @return event
     */
    EventFullDto getEventByIdPublic(int eventId, String uri, String ip);

    /**
     * Get events for current user
     * with pagination
     *
     * @param userId of user
     * @param from   index of the first element
     * @param size   number of elements to return
     * @return list of events
     */
    List<EventShortDto> getEventsByUser(int userId, Integer from, Integer size);

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
    List<EventFullDto> getEventsByAdmin(Integer[] users, String[] states, Integer[] categories,
                                        String rangeStart, String rangeEnd, Integer from, Integer size);


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
    List<EventShortDto> getEventsPublic(String text, Integer[] categories, Boolean paid,
                                        String rangeStart, String rangeEnd,
                                        Boolean onlyAvailable, String sort,
                                        Integer from, Integer size,
                                        HttpServletRequest request);

}
