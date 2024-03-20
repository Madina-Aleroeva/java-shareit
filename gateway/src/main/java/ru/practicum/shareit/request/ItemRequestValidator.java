package ru.practicum.shareit.request;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@Service
public class ItemRequestValidator {
    public void checkRequestId(Long userId) {
        if (userId == null) {
            throw new ValidationException("Request id should not be null");
        }
    }

    public void checkRequest(ItemRequestDto itemRequestDto) {
        if (itemRequestDto.getDescription() == null || itemRequestDto.getDescription().isBlank()) {
            throw new ValidationException("Request description should not be null");
        }
    }
}
