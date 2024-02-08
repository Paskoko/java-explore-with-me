package ru.practicum.event.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.event.model.Location;

/**
 * Repository interface for locations table
 */
public interface LocationRepository extends JpaRepository<Location, Integer> {
}
