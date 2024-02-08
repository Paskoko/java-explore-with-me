package ru.practicum.request;

import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.model.Request;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.util.Util.FORMATTER;

/**
 * Class mapper for request object
 */
public class RequestMapper {

    /**
     * Transform request to participationRequestDto object
     *
     * @param request to transform
     * @return participationRequestDto object
     */
    public static ParticipationRequestDto toParticipationRequestDto(Request request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .created(request.getCreated().format(FORMATTER))
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus().name())
                .build();
    }

    /**
     * Transform list of requests to list of participationRequestDto objects
     *
     * @param requests to transform
     * @return list of participationRequestDto objects
     */
    public static List<ParticipationRequestDto> toParticipationRequestDtoList(List<Request> requests) {
        return requests.stream().map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }
}
