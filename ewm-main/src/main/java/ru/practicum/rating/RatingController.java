package ru.practicum.rating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.rating.service.RatingService;

import java.util.List;

/**
 * Class controller for ratings
 */
@RestController
public class RatingController {

    private final RatingService ratingService;

    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    /**
     * POST request handler to create new rate for the event
     *
     * @param userId  of user
     * @param eventId of the event
     * @param rating  1-5
     * @return rated event
     */
    @PostMapping(value = "/users/{userId}/events/{eventId}/rate/{rating}")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createUser(@PathVariable int userId, @PathVariable int eventId, @PathVariable int rating) {
        return ratingService.setRating(userId, eventId, rating);
    }

    /**
     * DELETE request handler to delete rate for the event
     *
     * @param userId  of user
     * @param eventId of event
     */
    @DeleteMapping(value = "/users/{userId}/events/{eventId}/rate/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable int userId, @PathVariable int eventId) {
        ratingService.deleteRating(userId, eventId);
    }

    /**
     * GET request handler to get list of user's rated events
     *
     * @param userId of user
     * @return list of events
     */
    @GetMapping(value = "/users/{userId}/rates")
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getUserRatingByEvent(@PathVariable int userId) {
        return ratingService.getUserRatingByEvent(userId);
    }

}
