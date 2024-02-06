package ru.practicum.category.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * DTO class with category's components
 */
@Data
@Builder
public class CategoryDto {
    private int id;
    @NotBlank
    @Size(min = 1, max = 50)
    private String name;
}
