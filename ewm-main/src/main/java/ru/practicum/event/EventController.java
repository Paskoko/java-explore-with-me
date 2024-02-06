package ru.practicum.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.*;
import ru.practicum.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * Class controller for events
 */
@RestController
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * POST request handler to create new event
     * with validation
     *
     * @param newEvent to add
     * @param userId   of user
     * @return added event
     */
    @PostMapping(value = "/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@Valid @RequestBody NewEventDto newEvent,
                                    @PathVariable int userId) {
        return eventService.createEvent(newEvent, userId);
    }

    /**
     * PATCH request handler to update event by id
     * by user
     *
     * @param updateEvent to update
     * @param userId      of user
     * @param eventId     of the event
     * @return updated event
     */
    @PatchMapping(value = "/users/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEventByUser(@Valid @RequestBody UpdateEventUserRequest updateEvent,
                                          @PathVariable int userId,
                                          @PathVariable int eventId) {
        return eventService.updateEventByUser(updateEvent, userId, eventId);
    }

    /**
     * GET request handler to get event by id
     * by user
     *
     * @param userId  of user
     * @param eventId of event
     * @return event
     */
    @GetMapping(value = "/users/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventByIdByUser(@PathVariable int userId,
                                           @PathVariable int eventId) {
        return eventService.getEventById(userId, eventId);
    }

    /**
     * GET request handler to get list of events
     * with pagination
     * by user
     *
     * @param userId of user
     * @param from   index of the first element
     *               default value = 0
     * @param size   number of elements to return
     *               default value = 10
     * @return list of events
     */
    @GetMapping(value = "/users/{userId}/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEventsByUser(
            @PathVariable int userId,
            @RequestParam(value = "from", defaultValue = "0", required = false) Integer from,
            @RequestParam(value = "size", defaultValue = "10", required = false) Integer size) {
        return eventService.getEventsByUser(userId, from, size);
    }

    /**
     * GET request handler to get list of events
     * with pagination
     * by admin
     *
     * @param users      list of user's ids
     * @param states     list of states
     * @param categories list of categories
     * @param rangeStart start date
     * @param rangeEnd   end date
     * @param from       index of the first element
     *                   default value = 0
     * @param size       number of elements to return
     *                   default value = 10
     * @return list of events
     */
    @GetMapping(value = "/admin/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> getEventsByAdmin(
            @RequestParam(value = "users", required = false) Integer[] users,
            @RequestParam(value = "states", required = false) String[] states,
            @RequestParam(value = "categories", required = false) Integer[] categories,
            @RequestParam(value = "rangeStart", required = false) String rangeStart,
            @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
            @RequestParam(value = "from", defaultValue = "0", required = false) Integer from,
            @RequestParam(value = "size", defaultValue = "10", required = false) Integer size) {
        return eventService.getEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }


    /**
     * PATCH request handler to update compilation by id
     * by admin
     *
     * @param updateEvent to update
     * @param eventId     of the event
     * @return updated event
     */
    @PatchMapping(value = "/admin/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEventByAdmin(@Valid @RequestBody UpdateEventAdminRequest updateEvent,
                                           @PathVariable int eventId) {
        return eventService.updateEventByAdmin(updateEvent, eventId);
    }


    /**
     * GET request handler to get list of events
     * with pagination
     * by public
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
     *                      default value = 0
     * @param size          number of elements to return
     *                      default value = 10
     * @return list of events
     */
    @GetMapping(value = "/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEventsPublic(
            @RequestParam(value = "text", required = false) String text,
            @RequestParam(value = "categories", required = false) Integer[] categories,
            @RequestParam(value = "paid", required = false) Boolean paid,
            @RequestParam(value = "rangeStart", required = false) String rangeStart,
            @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
            @RequestParam(value = "onlyAvailable", defaultValue = "false", required = false) Boolean onlyAvailable,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "from", defaultValue = "0", required = false) Integer from,
            @RequestParam(value = "size", defaultValue = "10", required = false) Integer size,
            HttpServletRequest request) {
        return eventService.getEventsPublic(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size, request);
    }


    /**
     * GET request handler to get event by id
     * by public
     *
     * @param id of event
     * @return event
     */
    @GetMapping(value = "/events/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventByIdByUser(@PathVariable int id, HttpServletRequest request) {
        return eventService.getEventByIdPublic(id, request.getRequestURI(), request.getRemoteAddr());
    }

}
