package ru.practicum.rating.service;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;

import java.util.List;

/**
 * Interface for rating service
 */
public interface RatingService {

    /**
     * Set rating for the event
     *
     * @param userId      of user
     * @param eventId     of the event
     * @param eventRating 1-5
     * @return rated event
     */
    EventFullDto setRating(int userId, int eventId, int eventRating);

    /**
     * Delete rate for the event
     *
     * @param userId  of user
     * @param eventId of the event
     * @return event without user's rating
     */
    EventFullDto deleteRating(int userId, int eventId);

    /**
     * Get user's rated events
     *
     * @param userId of user
     * @return list of events
     */
    List<EventShortDto> getUserRatingByEvent(int userId);
}
