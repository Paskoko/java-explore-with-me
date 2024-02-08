package ru.practicum.request.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.request.model.Request;

/**
 * Repository interface for requests table
 */
public interface RequestRepository extends JpaRepository<Request, Integer>, QuerydslPredicateExecutor<Request> {
}
