package ru.practicum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * DTO class for statistics view
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewStatsDto {
    @NotBlank
    private String app;
    @NotBlank
    private String uri;
    @NotBlank
    private int hits;
}
