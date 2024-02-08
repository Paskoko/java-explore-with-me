package ru.practicum.category;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.model.Category;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Class mapper for category object
 */
public class CategoryMapper {

    /**
     * Transform category to categoryDto object
     *
     * @param category to transform
     * @return categoryDto object
     */
    public static CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    /**
     * Transform newCategoryDto to category object
     *
     * @param newCategoryDto to transform
     * @return category object
     */
    public static Category toCategory(NewCategoryDto newCategoryDto) {
        return Category.builder()
                .name(newCategoryDto.getName())
                .build();
    }

    /**
     * Transform newCategoryDto to category object
     *
     * @param categoryDto to transform
     * @return category object
     */
    public static Category toCategory(CategoryDto categoryDto) {
        return Category.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .build();
    }

    /**
     * Transform list of categories to list of categoryDto objects
     *
     * @param categoryList to transform
     * @return list of categoryDto objects
     */
    public static List<CategoryDto> toListCategoryDto(List<Category> categoryList) {
        return categoryList.stream().map(CategoryMapper::toCategoryDto).collect(Collectors.toList());
    }
}
