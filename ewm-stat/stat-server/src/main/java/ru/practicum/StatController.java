package ru.practicum;

import io.micrometer.core.lang.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.StatService;

import java.util.List;

/**
 * Class controller for statistics
 */
@RestController
public class StatController {
    private final StatService statService;

    @Autowired
    public StatController(StatService statService) {
        this.statService = statService;
    }

    /**
     * POST handler to save new hit to statistics
     *
     * @param endpointHitDto to save
     * @return saved object
     */
    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitDto saveHit(@RequestBody EndpointHitDto endpointHitDto) {
        return statService.saveHit(endpointHitDto);
    }

    /**
     * GET statistics with parameters
     *
     * @param start  of statistics
     * @param end    of statistics
     * @param uris   list of URIs of statistics
     * @param unique false - no need to show stats with unique IPs,
     *               true - show stats only with unique IPs
     * @return list of statistics
     */
    @GetMapping("/stats")
    public List<ViewStatsDto> getStat(@RequestParam(value = "start") String start,
                                      @RequestParam(value = "end") String end,
                                      @RequestParam(value = "uris", required = false) @Nullable String[] uris,
                                      @RequestParam(value = "unique", defaultValue = "false", required = false) boolean unique) {
        return statService.getStat(start, end, uris, unique);
    }

}
