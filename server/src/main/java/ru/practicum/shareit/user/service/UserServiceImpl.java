package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.DuplicateException;
import ru.practicum.shareit.exception.NotFoundException;
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
    public User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("not found user with id %d", userId)));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getUser(Long userId) {
        return userMapper.convertToDto(findUser(userId));
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        User user = userMapper.convertToUser(userDto);
        userRepository.save(user);
        userRepository.flush();
        if (user.getEmail() != null) {
            checkDuplicateAdd(user.getEmail(), user.getId());
        }
        return userMapper.convertToDto(user);
    }

    private User editUser(Long id, User editUser) {
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
    public UserDto editUser(Long userId, UserDto userDto) {
        User user = userMapper.convertToUser(userDto);
        if (user.getEmail() != null) {
            checkDuplicateEdit(user.getEmail(), userId);
        }

        return userMapper.convertToDto(editUser(userId, user));
    }

    @Override
    public void delUser(Long userId) {
        userRepository.deleteById(userId);
    }

    private void checkDuplicateAdd(String email, Long userId) {
        List<User> users = userRepository.findAllByEmail(email);
        if (!users.isEmpty() && !users.get(0).getId().equals(userId)) {
            userRepository.deleteById(userId);
            throw new DuplicateException("Email " + email + " already exists");
        }
    }

    private void checkDuplicateEdit(String email, Long userId) {
        List<User> users = userRepository.findAllByEmail(email);
        if (!users.isEmpty() && !users.get(0).getId().equals(userId)) {
            throw new DuplicateException("Email " + email + " already exists");
        }
    }


}
