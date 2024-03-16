package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    Booking findBooking(Integer bookingId);

    BookingDto createBooking(BookingDto bookingDto, Integer sharerId);

    BookingDto acceptBooking(Integer bookingId, Integer userId, Boolean approved);

    BookingDto getBooking(Integer bookingId, Integer userId);

    List<BookingDto> getBookerBookings(String status, Integer userId, Integer from, Integer size);

    List<BookingDto> getOwnerBookings(String status, Integer userId, Integer from, Integer size);
}
