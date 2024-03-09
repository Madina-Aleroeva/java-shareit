package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicateException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(UserMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getUser(int userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("not found user");
        }
        return UserMapper.convertToDto(user.get());
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.convertToUser(userDto);
        checkEmail(user.getEmail());
        log.debug("add user {}", user);
        userRepository.save(user);
        userRepository.flush();
        if (user.getEmail() != null) {
            checkDuplicateAdd(user.getEmail(), user.getId());
        }
        System.out.println("add user" + user);
        return UserMapper.convertToDto(user);
    }

    private User editUser(int id, User editUser) {
        User curUser = userRepository.findById(id).get();
        if (editUser.getEmail() != null) {
            curUser.setEmail(editUser.getEmail());
        }
        if (editUser.getName() != null) {
            curUser.setName(editUser.getName());
        }
        userRepository.save(curUser);
        userRepository.flush();
        System.out.println("edit user " + curUser);
        return curUser;
    }

    @Override
    public UserDto editUser(int userId, UserDto userDto) {
        System.out.println("start edit user " + userId);
        User user = UserMapper.convertToUser(userDto);
        if (user.getEmail() != null) {
            checkEmail(user.getEmail());
            checkDuplicateEdit(user.getEmail(), userId);
        }
        log.debug("edit user {}", user);

        return UserMapper.convertToDto(editUser(userId, user));
    }

    @Override
    public void delUser(int userId) {
        log.debug("del user {}", userId);
        System.out.println("delete user " + userId);
        userRepository.deleteById(userId);
    }

    private void checkDuplicateAdd(String email, int userId) {
        List<User> users = userRepository.findAllByEmail(email);
        if (!users.isEmpty() && users.get(0).getId() != userId) {
            System.out.println("found duplicates: " + users.get(0).getId() + ", " + userId);
            userRepository.deleteById(userId);
            System.out.println("delete user " + userId);
            throw new DuplicateException("Email " + email + " already exists");
        }
    }

    private void checkDuplicateEdit(String email, int userId) {
        List<User> users = userRepository.findAllByEmail(email);
        if (!users.isEmpty() && users.get(0).getId() != userId) {
            System.out.println("found duplicates: " + users.get(0).getId() + ", " + userId);
            throw new DuplicateException("Email " + email + " already exists");
        }
    }

    private void checkEmail(String email) {
        if (email == null) {
            throw new ValidationException("Email is empty");
        }
        if (!email.contains("@")) {
            throw new ValidationException("Email should contain '@'");
        }

    }
}
