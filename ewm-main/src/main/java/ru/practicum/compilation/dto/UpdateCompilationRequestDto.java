package ru.practicum.compilation.dto;

import lombok.Data;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * DTO class with compilation's components for update request
 */
@Data
public class UpdateCompilationRequestDto {
    private Boolean pinned;
    @Size(min = 1, max = 50)
    private String title;
    private List<Integer> events;
}
