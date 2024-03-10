package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.BookingStatusDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookingValidator {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public BookingStatusDto getStatus(String status) {
        try {
            return BookingStatusDto.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Unknown state: " + status);
        }
    }

    public void checkSharerId(int sharerId) {
        if (userRepository.findById(sharerId).isEmpty()) {
            throw new NotFoundException("not found sharer id");
        }
    }

    public void checkItemId(int itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("item not found"));

        if (!item.getAvailable()) {
            throw new ValidationException("item is not available");
        }
    }

    public void checkDates(BookingDto booking) {
        LocalDateTime start = booking.getStart();
        LocalDateTime end = booking.getEnd();
        LocalDateTime now = LocalDateTime.now();

        if (start == null || end == null) {
            throw new ValidationException("date can't be null");
        }
        if (end.isBefore(now)) {
            throw new ValidationException("end in past");
        }
        if (end.isBefore(start)) {
            throw new ValidationException("end before past");
        }
        if (end.isEqual(start)) {
            throw new ValidationException("end equal start");
        }
        if (start.isBefore(now)) {
            throw new ValidationException("start in past");
        }
    }
}
