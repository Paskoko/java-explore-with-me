package ru.practicum.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Class controller for events for public
 */
@RestController
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
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
