package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    User findUser(Integer userId);

    List<UserDto> getAllUsers();

    UserDto getUser(Integer userId);

    UserDto addUser(UserDto user);

    UserDto editUser(Integer userId, UserDto user);

    void delUser(Integer userId);
}
