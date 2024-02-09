package ru.practicum.user;

import ru.practicum.user.dto.NewUserRequestDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Class mapper for user object
 */
public class UserMapper {

    /**
     * Transform user to userDto object
     *
     * @param user to transform
     * @return userDto object
     */
    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .rating(user.getRating())
                .build();
    }

    /**
     * Transform newUserRequestDto to user object
     *
     * @param newUserRequestDto to transform
     * @return user object
     */
    public static User toUser(NewUserRequestDto newUserRequestDto) {
        return User.builder()
                .email(newUserRequestDto.getEmail())
                .name(newUserRequestDto.getName())
                .build();
    }

    /**
     * Transform user to userShortDto object
     *
     * @param user to transform
     * @return userShortDto object
     */
    public static UserShortDto toUserShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    /**
     * Transform list of user to list of userDto objects
     *
     * @param userList to transform
     * @return list of userDto objects
     */
    public static List<UserDto> toListUserDto(List<User> userList) {
        return userList.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }
}
