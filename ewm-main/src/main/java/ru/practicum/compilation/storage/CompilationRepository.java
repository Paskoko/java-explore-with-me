package ru.practicum.compilation.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.compilation.model.Compilation;

/**
 * Repository interface for compilations table
 */
public interface CompilationRepository extends JpaRepository<Compilation, Integer>, QuerydslPredicateExecutor<Compilation> {
}
