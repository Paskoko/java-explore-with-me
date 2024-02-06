package ru.practicum.user.service;

import ru.practicum.user.dto.NewUserRequestDto;
import ru.practicum.user.dto.UserDto;

import java.util.List;

/**
 * Interface for user service
 */
public interface UserService {

    /**
     * Add new user with validation
     *
     * @param user to add
     * @return added user
     */
    UserDto createUser(NewUserRequestDto user);

    /**
     * Delete user by id
     *
     * @param id of user
     */
    void deleteUser(int id);

    /**
     * Get list of users
     * with parameters
     *
     * @param ids  of users, if empty - return all users
     * @param from index of the first element
     * @param size number of elements to return
     * @return list of users
     */
    List<UserDto> getUsers(Integer[] ids, Integer from, Integer size);
}
