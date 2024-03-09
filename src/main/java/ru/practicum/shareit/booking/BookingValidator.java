package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingValidator {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public void checkSharerId(int sharerId) {
        if (userRepository.findById(sharerId).isEmpty())
            throw new NotFoundException("not found sharer id");
    }

    public void checkItemId(int itemId) {
        Optional<Item> item = itemRepository.findById(itemId);

        if (item.isEmpty()) {
            throw new NotFoundException("item not found");
        }

        if (!item.get().getAvailable()) {
            throw new ValidationException("item is not available");
        }
    }

    public void checkDates(BookingDto booking) {
        LocalDateTime start = booking.getStart();
        LocalDateTime end = booking.getEnd();

        if (start == null || end == null) {
            throw new ValidationException("date can't be null");
        }

        if (end.isBefore(LocalDateTime.now())) {
            throw new ValidationException("end in past");
        }
        if (end.isBefore(start)) {
            throw new ValidationException("end before past");
        }
        if (end.isEqual(start)) {
            throw new ValidationException("end equal start");
        }
        if (start.isBefore(LocalDateTime.now())) {
            throw new ValidationException("start in past");
        }
    }
}
