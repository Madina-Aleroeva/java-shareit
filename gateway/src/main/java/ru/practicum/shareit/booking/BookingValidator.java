package ru.practicum.shareit.booking;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;

import java.time.LocalDateTime;

@Service
public class BookingValidator {

    public void checkStatus(String status) {
        try {
            BookingStatusDTO.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Unknown state: " + status);
        }
    }

    public void checkPage(Long from, Long size) {
        if (size != null || from != null) {
            if (size == null || size <= 0) {
                throw new ValidationException("size should be > 0");
            }
            if (from == null || from < 0) {
                throw new ValidationException("from should be >= 0");
            }
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
