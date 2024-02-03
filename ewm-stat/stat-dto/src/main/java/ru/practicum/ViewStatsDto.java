package ru.practicum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * DTO class for statistics view
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewStatsDto {
    @NotBlank
    private String app;
    @NotBlank
    private String uri;
    @NotBlank
    private long hits;
}
