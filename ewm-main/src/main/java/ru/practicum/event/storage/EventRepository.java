package ru.practicum.event.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.event.model.Event;

import java.util.List;

/**
 * Repository interface for events table
 */
public interface EventRepository extends JpaRepository<Event, Integer>, QuerydslPredicateExecutor<Event> {

    /**
     * Find events by list of ids
     *
     * @param ids of users to find
     * @return list of users
     */
    List<Event> findAllByIdIn(List<Integer> ids);
}
