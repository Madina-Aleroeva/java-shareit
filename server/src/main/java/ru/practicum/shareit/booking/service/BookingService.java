package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    Booking findBooking(Long bookingId);

    BookingDto createBooking(BookingDto bookingDto, Long sharerId);

    BookingDto acceptBooking(Long bookingId, Long userId, Boolean approved);

    BookingDto getBooking(Long bookingId, Long userId);

    List<BookingDto> getBookerBookings(String status, Long userId, Long from, Long size);

    List<BookingDto> getOwnerBookings(String status, Long userId, Long from, Long size);
}
