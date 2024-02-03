package ru.practicum.service;

import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;

import java.util.List;

/**
 * Interface for statistics
 */
public interface StatService {

    /**
     * Save new hit to statistics
     *
     * @param endpointHitDto to send
     * @return saved object
     */
    EndpointHitDto saveHit(EndpointHitDto endpointHitDto);

    /**
     * Get statistics by parameters
     *
     * @param start  of statistics
     * @param end    of statistics
     * @param uris   list of URIs of statistics
     * @param unique false - no need to show stats with unique IPs,
     *               true - show stats only with unique IPs
     * @return list of statistics
     */
    List<ViewStatsDto> getStat(String start, String end, String[] uris, boolean unique);

}
