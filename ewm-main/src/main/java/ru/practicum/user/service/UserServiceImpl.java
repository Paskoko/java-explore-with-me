package ru.practicum.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.user.UserMapper;
import ru.practicum.user.dto.NewUserRequestDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.storage.UserRepository;
import ru.practicum.util.exception.ResourceNotFoundException;

import java.util.Arrays;
import java.util.List;

/**
 * Class service for operations with users storage
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Add new user with validation
     *
     * @param user to add
     * @return added user
     */
    @Override
    public UserDto createUser(NewUserRequestDto user) {
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(user)));
    }

    /**
     * Delete user by id
     *
     * @param id of user
     */
    @Override
    public void deleteUser(int id) {
        if (userRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("User with id=" + id + " was not found.");
        }
        userRepository.deleteById(id);
    }

    /**
     * Get list of users with parameters
     *
     * @param ids  of users, if empty - return all users
     * @param from index of the first element
     * @param size number of elements to return
     * @return list of users
     */
    @Override
    public List<UserDto> getUsers(Integer[] ids, Integer from, Integer size) {
        PageRequest page = PageRequest.of(from / size, size);
        if (ids == null) {
            return UserMapper.toListUserDto(userRepository.findAll(page).getContent());
        } else {
            return UserMapper.toListUserDto(userRepository.findByIdIn(Arrays.asList(ids), page));
        }
    }
}
