package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private static final String SHARER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public BookingDto createBooking(@RequestBody BookingDto booking,
                                    @RequestHeader(value = SHARER_USER_ID, required = false) Integer sharerId) {
        return bookingService.createBooking(booking, sharerId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto acceptBooking(@PathVariable Integer bookingId,
                                    @RequestParam boolean approved,
                                    @RequestHeader(value = SHARER_USER_ID, required = false) Integer sharerId) {
        return bookingService.acceptBooking(bookingId, sharerId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@PathVariable Integer bookingId,
                                 @RequestHeader(value = SHARER_USER_ID, required = false) Integer sharerId) {
        return bookingService.getBooking(bookingId, sharerId);
    }

    @GetMapping()
    public List<BookingDto> getBookerBookings(@RequestParam(defaultValue = "ALL") String state,
                                              @RequestHeader(value = SHARER_USER_ID, required = false) Integer sharerId) {
        return bookingService.getBookerBookings(state, sharerId);
    }

    @GetMapping("/owner")
    public List<BookingDto> getOwnerBookings(@RequestParam(defaultValue = "ALL") String state,
                                             @RequestHeader(value = SHARER_USER_ID, required = false) Integer sharerId) {
        return bookingService.getOwnerBookings(state, sharerId);
    }
}













