package ru.practicum.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * DTO class with short user's components
 */
@Data
@Builder
public class UserShortDto {
    @NotBlank
    private int id;
    @NotNull
    private String name;
}
