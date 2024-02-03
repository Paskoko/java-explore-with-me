package ru.practicum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

/**
 * Class client to prepare REST requests to server and handle requests from main
 */
@Service
public class StatClient extends BaseClient {
    @Autowired
    public StatClient(@Value("${ewm-stat-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    /**
     * Handle post request to save hit entity
     * Map input data to DTO
     *
     * @param app       for what service
     * @param uri       of a request
     * @param ip        of a user
     * @param timeStamp when it was sent
     * @return response from stat server
     */
    public ResponseEntity<Object> saveHit(String app, String uri, String ip, String timeStamp) {
        EndpointHitDto endpointHitDto;
        return post("/hit", EndpointHitDto.builder()
                .app(app)
                .uri(uri)
                .ip(ip)
                .timeStamp(timeStamp));
    }
}
