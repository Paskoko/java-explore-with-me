package ru.practicum.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * DTO class with user's components
 */
@Data
@Builder
public class UserDto {
    private int id;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String name;
}
