package ru.practicum.shareit.booking;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;

@Service
public class PageValidation {
    public void checkPage(Integer from, Integer size) {
        if (size != null || from != null) {
            if (size == null || size <= 0) {
                throw new ValidationException("size should be > 0");
            }
            if (from == null || from < 0) {
                throw new ValidationException("from should be > 0");
            }
        }
    }
}
