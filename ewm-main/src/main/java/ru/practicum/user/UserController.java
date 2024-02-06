package ru.practicum.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.NewUserRequestDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * Class controller for users
 */
@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * POST request handler to create new user
     * with validation
     *
     * @param newUser to add
     * @return added user
     */
    @PostMapping(value = "/admin/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody NewUserRequestDto newUser) {
        return userService.createUser(newUser);
    }

    /**
     * DELETE request handler to delete user by id
     *
     * @param userId of user to delete
     */
    @DeleteMapping(value = "/admin/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);
    }

    /**
     * GET request handler to get list of users
     * with pagination
     *
     * @param ids  of users to find, if empty - return all users
     * @param from index of the first element
     *             default value = 0
     * @param size number of elements to return
     *             default value = 10
     * @return list of users
     */
    @GetMapping(value = "/admin/users")
    public List<UserDto> getUsers(@RequestParam(value = "ids", required = false) Integer[] ids,
                                  @RequestParam(value = "from", defaultValue = "0", required = false) Integer from,
                                  @RequestParam(value = "size", defaultValue = "10", required = false) Integer size) {
        return userService.getUsers(ids, from, size);
    }

}
