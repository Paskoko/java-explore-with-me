package ru.practicum.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.service.EventService;

import javax.validation.Valid;
import java.util.List;

/**
 * Class controller for events for private
 */
@RestController
public class PrivateEventController {

    private final EventService eventService;

    @Autowired
    public PrivateEventController(EventService eventService) {
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
}
