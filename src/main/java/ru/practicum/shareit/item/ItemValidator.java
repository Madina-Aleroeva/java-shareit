package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;

@RequiredArgsConstructor
@Service
public class ItemValidator {
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
