package ru.practicum.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.service.CategoryService;

import javax.validation.Valid;

/**
 * Class controller for categories for admin
 */
@RestController
public class AdminCategoryController {
    private final CategoryService categoryService;

    @Autowired
    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * POST request handler to create new category
     * with validation
     *
     * @param newCategory to add
     * @return added category
     */
    @PostMapping(value = "/admin/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody NewCategoryDto newCategory) {
        return categoryService.createCategory(newCategory);
    }

    /**
     * DELETE request handler to delete category by id
     *
     * @param catId of category to delete
     */
    @DeleteMapping(value = "/admin/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable int catId) {
        categoryService.deleteCategory(catId);
    }


    /**
     * PATCH request handler to update category by id
     *
     * @param categoryDto to update
     * @param catId       of the category
     * @return updated category
     */
    @PatchMapping(value = "/admin/categories/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable int catId) {
        return categoryService.updateCategory(catId, categoryDto);
    }
}
