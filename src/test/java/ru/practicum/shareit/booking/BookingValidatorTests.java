package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.BookingStatusDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.ValidationException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class BookingValidatorTests {
    @Autowired
    private BookingValidator bookingValidator;
    private BookingDto bookingDto;

    @BeforeEach
    void init() {
        bookingDto = BookingDto.builder().build();
    }

    @Test
    void getStatusValidTest() {
        assertEquals(BookingStatusDto.ALL, bookingValidator.getStatus("ALL"));
    }

    @Test
    void getStatusInvalidTest() {
        assertThrows(ValidationException.class, () -> bookingValidator.getStatus("INVALID_STATUS"));
    }

    @Test
    void checkDatesStartNullTest() {
        bookingDto.setStart(null);
        bookingDto.setEnd(LocalDateTime.now().plusDays(1));

        assertThrows(ValidationException.class, () -> bookingValidator.checkDates(bookingDto));
    }

    @Test
    void checkDatesEndNullTest() {
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(null);

        assertThrows(ValidationException.class, () -> bookingValidator.checkDates(bookingDto));
    }

    @Test
    void checkDatesEndBeforeStartTest() {
        LocalDateTime now = LocalDateTime.now();
        bookingDto.setStart(now.plusDays(1));
        bookingDto.setEnd(now);

        assertThrows(ValidationException.class, () -> bookingValidator.checkDates(bookingDto));
    }

    @Test
    void checkDatesEndEqualStartTest() {
        LocalDateTime now = LocalDateTime.now();
        bookingDto.setStart(now);
        bookingDto.setEnd(now);

        assertThrows(ValidationException.class, () -> bookingValidator.checkDates(bookingDto));
    }

    @Test
    void checkDatesStartInPastTest() {
        LocalDateTime now = LocalDateTime.now();
        bookingDto.setStart(now.minusDays(1));
        bookingDto.setEnd(now.plusDays(1));

        assertThrows(ValidationException.class, () -> bookingValidator.checkDates(bookingDto));
    }

    @Test
    void checkDatesValidTest() {
        LocalDateTime now = LocalDateTime.now();
        bookingDto.setStart(now.plusDays(1));
        bookingDto.setEnd(now.plusDays(2));

        assertDoesNotThrow(() -> bookingValidator.checkDates(bookingDto));
    }
}
