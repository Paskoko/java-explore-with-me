package ru.practicum.compilation.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.event.dto.EventShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * DTO class with compilation's components
 */
@Data
@Builder
public class CompilationDto {
    @NotNull
    private int id;
    @NotBlank
    private boolean pinned;
    @NotBlank
    private String title;
    private List<EventShortDto> events;
}
