package ru.practicum.shareit.user.dto;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;

@Service
public class UserMapper {
    public User convertToUser(UserDto obj) {
        return new User(obj.getId(), obj.getName(), obj.getEmail());
    }

    public UserDto convertToDto(User obj) {
        return new UserDto(obj.getId(), obj.getName(), obj.getEmail());
    }
}
