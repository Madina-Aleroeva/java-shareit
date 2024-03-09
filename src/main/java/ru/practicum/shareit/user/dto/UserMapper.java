package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.User;

public class UserMapper {
    public static User convertToUser(UserDto userDto) {
        return new User(userDto.getId(), userDto.getName(), userDto.getEmail());
    }

    public static UserDto convertToDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }
}
