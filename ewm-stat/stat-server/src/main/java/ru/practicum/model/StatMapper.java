package ru.practicum.model;

import ru.practicum.EndpointHitDto;

import java.time.LocalDateTime;

import static ru.practicum.util.Util.formatter;

/**
 * Class mapper for statistics object
 */
public class StatMapper {

    /**
     * Transform from EndpointHitDto to Statistics object
     *
     * @param endpointHitDto to transform
     * @return Statistics object
     */
    public static Statistics toStatistics(EndpointHitDto endpointHitDto) {
        return Statistics.builder()
                .app(endpointHitDto.getApp())
                .uri(endpointHitDto.getUri())
                .ip(endpointHitDto.getIp())
                .timeStamp(LocalDateTime.parse(endpointHitDto.getTimeStamp(), formatter))
                .build();
    }

    /**
     * Transform from Statistics to EndpointHitDto object
     *
     * @param statistics to transform
     * @return EndpointHitDto object
     */
    public static EndpointHitDto toEndpointHitDto(Statistics statistics) {
        return EndpointHitDto.builder()
                .app(statistics.getApp())
                .uri(statistics.getUri())
                .ip(statistics.getIp())
                .timeStamp(statistics.getTimeStamp().format(formatter))
                .build();
    }
}
