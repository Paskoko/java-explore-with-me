package ru.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.model.StatMapper;
import ru.practicum.storage.StatRepository;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.util.Util.FORMATTER;

/**
 * Class service for operations with statistics
 */
@Service
public class StatServiceImpl implements StatService {

    private final StatRepository statRepository;

    @Autowired
    public StatServiceImpl(StatRepository statRepository) {
        this.statRepository = statRepository;
    }


    /**
     * Save new hit to statistics
     *
     * @param endpointHitDto to send
     * @return saved object
     */
    @Override
    public EndpointHitDto saveHit(EndpointHitDto endpointHitDto) {
        return StatMapper.toEndpointHitDto(statRepository.save(StatMapper.toStatistics(endpointHitDto)));
    }

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
    @Override
    public List<ViewStatsDto> getStat(String start, String end, String[] uris, boolean unique) {
        if (LocalDateTime.parse(start, FORMATTER).isAfter(LocalDateTime.parse(end, FORMATTER))) {
            throw new ValidationException("Start time should be before end time.");
        }

        if (unique) {
            if (uris != null) {
                return statRepository.getAllWithUniqueIp(LocalDateTime.parse(start, FORMATTER),
                        LocalDateTime.parse(end, FORMATTER), List.of(uris));
            } else {
                return statRepository.getAllWithUniqueIpAndWithoutUris(LocalDateTime.parse(start, FORMATTER),
                        LocalDateTime.parse(end, FORMATTER));
            }
        } else {
            if (uris != null) {
                return statRepository.getAllWithAllIp(LocalDateTime.parse(start, FORMATTER),
                        LocalDateTime.parse(end, FORMATTER), List.of(uris));
            } else {
                return statRepository.getAllWithAllIpAndWithoutUris(LocalDateTime.parse(start, FORMATTER),
                        LocalDateTime.parse(end, FORMATTER));
            }
        }
    }
}
