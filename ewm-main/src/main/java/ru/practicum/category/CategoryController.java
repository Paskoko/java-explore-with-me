package ru.practicum.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.service.CategoryService;

import java.util.List;

/**
 * Class controller for categories for public
 */
@RestController
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
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
