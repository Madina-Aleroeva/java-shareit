package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    User findUser(Long userId);

    List<UserDto> getAllUsers();

    UserDto getUser(Long userId);

    UserDto addUser(UserDto user);

    UserDto editUser(Long userId, UserDto user);

    void delUser(Long userId);
}
