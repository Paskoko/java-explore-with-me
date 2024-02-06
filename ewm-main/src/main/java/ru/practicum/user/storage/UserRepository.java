package ru.practicum.user.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.user.model.User;

import java.util.List;

/**
 * Repository interface for users table
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    /**
     * Find users by list of ids with pagination
     *
     * @param ids      of users to find
     * @param pageable page request
     * @return list of users
     */
    List<User> findByIdIn(List<Integer> ids, Pageable pageable);

}
