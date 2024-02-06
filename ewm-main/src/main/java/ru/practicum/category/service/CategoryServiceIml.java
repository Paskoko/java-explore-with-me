package ru.practicum.category.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.category.CategoryMapper;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.model.Category;
import ru.practicum.category.storage.CategoryRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.QEvent;
import ru.practicum.event.storage.EventRepository;
import ru.practicum.util.exception.NotEmptyCategoryException;
import ru.practicum.util.exception.ResourceNotFoundException;

import java.util.List;

/**
 * Class service for operations with categories storage
 */
@Service
public class CategoryServiceIml implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Autowired
    public CategoryServiceIml(CategoryRepository categoryRepository, EventRepository eventRepository) {
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
    }

    /**
     * Add new category
     *
     * @param category to add
     * @return added category
     */
    @Override
    public CategoryDto createCategory(NewCategoryDto category) {
        return CategoryMapper.toCategoryDto(categoryRepository.save(CategoryMapper.toCategory(category)));
    }

    /**
     * Delete category by id
     *
     * @param id of category
     */
    @Override
    public void deleteCategory(int id) {
        Category category = categoryRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Category with id=" + id + " was not found."));

        // Check events connected with category
        BooleanExpression byCategory = QEvent.event.category.eq(category);
        List<Event> events = (List<Event>) eventRepository.findAll(byCategory);
        if (!events.isEmpty()) {
            throw new NotEmptyCategoryException("The category is not empty.");
        }
        categoryRepository.deleteById(id);
    }

    /**
     * Update category
     *
     * @param id          of category
     * @param categoryDto to update
     * @return updated category
     */
    @Override
    public CategoryDto updateCategory(int id, CategoryDto categoryDto) {
        if (categoryRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Category with id=" + id + " was not found.");
        }
        categoryDto.setId(id);  // add id to validate in repository save method (if id exists - merge in table)
        return CategoryMapper.toCategoryDto(categoryRepository.save(CategoryMapper.toCategory(categoryDto)));
    }

    /**
     * Get category by id
     *
     * @param id of category
     * @return category
     */
    @Override
    public CategoryDto getCategoryById(int id) {
        return CategoryMapper.toCategoryDto(categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id=" + id + " was not found.")));
    }

    /**
     * Get all categories
     * with pagination
     *
     * @param from index of the first element
     * @param size number of elements to return
     * @return list of categories
     */
    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        PageRequest page = PageRequest.of(from / size, size);
        return CategoryMapper.toListCategoryDto(categoryRepository.findAll(page).getContent());
    }
}
