package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    UserDto getUser(int userId);

    UserDto addUser(UserDto user);

    UserDto editUser(int userId, UserDto user);

    void delUser(int userId);
}
