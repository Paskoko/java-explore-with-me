package ru.practicum.rating.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.rating.model.Rating;

/**
 * Repository interface for ratings table
 */
public interface RatingRepository extends JpaRepository<Rating, Integer>, QuerydslPredicateExecutor<Rating> {
}
