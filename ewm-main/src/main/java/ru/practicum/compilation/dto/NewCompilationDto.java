package ru.practicum.compilation.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * DTO class with compilation's components for new requests
 */
@Data
public class NewCompilationDto {
    private boolean pinned;
    @NotBlank
    @Size(min = 1, max = 50)
    private String title;
    private List<Integer> events;
}
