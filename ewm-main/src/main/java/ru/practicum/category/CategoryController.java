package ru.practicum.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.service.CategoryService;

import javax.validation.Valid;
import java.util.List;

/**
 * Class controller for categories
 */
@RestController
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
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

    /**
     * GET request handler to get list of categories
     * with pagination
     *
     * @param from index of the first element
     *             default value = 0
     * @param size number of elements to return
     *             default value = 10
     * @return list of categories
     */
    @GetMapping(value = "/categories")
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDto> getCategories(
            @RequestParam(value = "from", defaultValue = "0", required = false) Integer from,
            @RequestParam(value = "size", defaultValue = "10", required = false) Integer size) {
        return categoryService.getCategories(from, size);
    }

    /**
     * GET request handler to get category by id
     *
     * @param catId of the category
     * @return category
     */
    @GetMapping(value = "/categories/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto getCategory(@PathVariable int catId) {
        return categoryService.getCategoryById(catId);
    }

}
