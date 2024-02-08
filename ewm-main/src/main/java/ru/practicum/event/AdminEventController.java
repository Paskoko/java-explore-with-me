package ru.practicum.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.service.EventService;

import javax.validation.Valid;
import java.util.List;

/**
 * Class controller for events for admin
 */
@RestController
public class AdminEventController {

    private final EventService eventService;

    @Autowired
    public AdminEventController(EventService eventService) {
        this.eventService = eventService;
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
}
