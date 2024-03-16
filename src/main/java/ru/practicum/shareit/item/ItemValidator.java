package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.UserRepository;

@RequiredArgsConstructor
@Service
public class ItemValidator {
    private final UserRepository userRepository;

    public void checkItemFields(Item item) {
        if (item.getName() == null || item.getName().isBlank()) {
            throw new ValidationException("Item should have name");
        }
        if (item.getDescription() == null || item.getDescription().isBlank()) {
            throw new ValidationException("Item should have description");
        }
        if (item.getAvailable() == null) {
            throw new ValidationException("Item should have available");
        }
    }

}
