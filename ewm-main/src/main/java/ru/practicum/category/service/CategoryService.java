package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;

import java.util.List;

/**
 * Interface for category service
 */
public interface CategoryService {

    /**
     * Add new category
     *
     * @param category to add
     * @return added category
     */
    CategoryDto createCategory(NewCategoryDto category);

    /**
     * Delete category by id
     *
     * @param id of category
     */
    void deleteCategory(int id);

    /**
     * Update category
     *
     * @param id          of category
     * @param categoryDto to update
     * @return updated category
     */
    CategoryDto updateCategory(int id, CategoryDto categoryDto);

    /**
     * Get category by id
     *
     * @param id of category
     * @return category
     */
    CategoryDto getCategoryById(int id);

    /**
     * Get all categories
     * with pagination
     *
     * @param from index of the first element
     * @param size number of elements to return
     * @return list of categories
     */
    List<CategoryDto> getCategories(Integer from, Integer size);
}
