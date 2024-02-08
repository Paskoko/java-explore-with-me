package ru.practicum.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * DTO class for new user requests
 */
@Data
public class NewUserRequestDto {
    @Email
    @NotBlank
    @Size(min = 6, max = 254)
    private String email;
    @NotBlank
    @Size(min = 2, max = 250)
    private String name;
}
