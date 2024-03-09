package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingServiceImpl bookingService;
    private static final String SHARER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public Booking createBooking(@RequestBody BookingDto booking,
                                 @RequestHeader(value = SHARER_USER_ID, required = false) Integer sharerId) {
        return bookingService.createBooking(booking, sharerId);
    }

    @PatchMapping("/{bookingId}")
    public Booking acceptBooking(@PathVariable int bookingId,
                                 @RequestParam boolean approved,
                                 @RequestHeader(value = SHARER_USER_ID, required = false) Integer sharerId) {
        return bookingService.acceptBooking(bookingId, sharerId, approved);
    }

    @GetMapping("/{bookingId}")
    public Booking getBooking(@PathVariable int bookingId,
                              @RequestHeader(value = SHARER_USER_ID, required = false) Integer sharerId) {
        return bookingService.getBooking(bookingId, sharerId);
    }

    @GetMapping()
    public List<Booking> getBookerBookings(@RequestParam(defaultValue = "ALL") String state,
                                           @RequestHeader(value = SHARER_USER_ID, required = false) Integer sharerId) {
        return bookingService.getBookerBookings(state, sharerId);
    }

    @GetMapping("/owner")
    public List<Booking> getOwnerBookings(@RequestParam(defaultValue = "ALL") String state,
                                          @RequestHeader(value = SHARER_USER_ID, required = false) Integer sharerId) {
        return bookingService.getOwnerBookings(state, sharerId);
    }
}













