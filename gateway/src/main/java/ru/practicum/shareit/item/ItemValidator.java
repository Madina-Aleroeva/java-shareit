package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

@RequiredArgsConstructor
@Service
public class ItemValidator {
    public void checkItemId(Long userId) {
        if (userId == null) {
            throw new ValidationException("Item id should not be null");
        }
    }

    public void checkAddItem(ItemDto itemDto) {
        if (itemDto.getName() == null || itemDto.getName().isBlank()) {
            throw new ValidationException("Item should have name");
        }
        if (itemDto.getDescription() == null || itemDto.getDescription().isBlank()) {
            throw new ValidationException("Item should have description");
        }
        if (itemDto.getAvailable() == null) {
            throw new ValidationException("Item should be available");
        }
    }

    public void checkComment(CommentDto commentDto) {
        if (commentDto.getText() == null || commentDto.getText().isBlank()) {
            throw new ValidationException("Comment should have text");
        }
    }

    public void checkEditItem(ItemDto itemDto) {
        if (itemDto.getName() != null && itemDto.getName().isBlank()) {
            throw new ValidationException("Item should have name");
        }
        if (itemDto.getDescription() != null && itemDto.getDescription().isBlank()) {
            throw new ValidationException("Item should have description");
        }
    }
}
