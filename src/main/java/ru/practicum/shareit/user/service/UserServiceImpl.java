package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.DuplicateException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public User findUser(Integer userId) {
        if (userId == null) {
            throw new ValidationException("user id can't be null");
        }

        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("not found user with id %d", userId)));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getUser(Integer userId) {
        return userMapper.convertToDto(findUser(userId));
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        User user = userMapper.convertToUser(userDto);
        checkEmail(user.getEmail());

        userRepository.save(user);
        userRepository.flush();
        if (user.getEmail() != null) {
            checkDuplicateAdd(user.getEmail(), user.getId());
        }
        return userMapper.convertToDto(user);
    }

    private User editUser(Integer id, User editUser) {
        User curUser = findUser(id);
        if (editUser.getEmail() != null) {
            curUser.setEmail(editUser.getEmail());
        }
        if (editUser.getName() != null) {
            curUser.setName(editUser.getName());
        }
        userRepository.save(curUser);
        userRepository.flush();
        return curUser;
    }

    @Override
    public UserDto editUser(Integer userId, UserDto userDto) {
        User user = userMapper.convertToUser(userDto);
        if (user.getEmail() != null) {
            checkEmail(user.getEmail());
            checkDuplicateEdit(user.getEmail(), userId);
        }

        return userMapper.convertToDto(editUser(userId, user));
    }

    @Override
    public void delUser(Integer userId) {
        userRepository.deleteById(userId);
    }

    private void checkDuplicateAdd(String email, Integer userId) {
        List<User> users = userRepository.findAllByEmail(email);
        if (!users.isEmpty() && !users.get(0).getId().equals(userId)) {
            userRepository.deleteById(userId);
            throw new DuplicateException("Email " + email + " already exists");
        }
    }

    private void checkDuplicateEdit(String email, Integer userId) {
        List<User> users = userRepository.findAllByEmail(email);
        if (!users.isEmpty() && !users.get(0).getId().equals(userId)) {
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
