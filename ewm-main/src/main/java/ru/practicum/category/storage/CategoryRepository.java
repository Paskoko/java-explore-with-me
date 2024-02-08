package ru.practicum.category.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.category.model.Category;

/**
 * Repository interface for categories table
 */
public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
