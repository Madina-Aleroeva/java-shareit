package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicateException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.getAllUsers().stream().map(UserMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getUser(int userId) {
        return UserMapper.convertToDto(userRepository.getUser(userId));
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.convertToUser(userDto);
        checkEmail(user.getEmail(), user.getId());
        log.debug("add user {}", user);
        return UserMapper.convertToDto(userRepository.addUser(user));
    }

    @Override
    public UserDto editUser(int userId, UserDto userDto) {
        User user = UserMapper.convertToUser(userDto);
        if (user.getEmail() != null) {
            checkEmail(user.getEmail(), userId);
        }
        log.debug("edit user {}", user);
        return UserMapper.convertToDto(userRepository.editUser(userId, user));
    }

    @Override
    public void delUser(int userId) {
        log.debug("del user {}", userId);
        userRepository.delUser(userId);
    }

    private void checkEmail(String email, int userId) {
        if (email == null) {
            throw new ValidationException("Email is empty");
        }
        if (!email.contains("@")) {
            throw new ValidationException("Email should contain '@'");
        }
        List<User> users = userRepository.getUserByEmail(email);
        if (!users.isEmpty() && users.get(0).getId() != userId) {
            throw new DuplicateException("Email " + email + " already exists");
        }
    }
}
