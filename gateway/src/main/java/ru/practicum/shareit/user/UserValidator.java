package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;

@Service
public class UserValidator {

    public void checkUserId(Long userId) {
        if (userId == null) {
            throw new ValidationException("User id should not be null");
        }
    }

    public void checkUserEmail(UserDto userDto) {
        Long id = userDto.getId();
        String email = userDto.getEmail();
        if (id == null && email == null) {
            throw new ValidationException("User email should not be null");
        }
        if (email != null && !email.contains("@")) {
            throw new ValidationException("User email should contain '@'");
        }
    }

}
