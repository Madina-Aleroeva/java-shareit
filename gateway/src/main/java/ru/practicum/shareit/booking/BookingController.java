package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/bookings")
@Slf4j
@RequiredArgsConstructor
public class BookingController {
    private final BookingClient bookingClient;
    private static final String SHARER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestBody BookingDto booking,
                                                @RequestHeader(value = SHARER_USER_ID, required = false) Long userId) {
        log.info("Create booking {}, userId = {}", booking, userId);
        return bookingClient.bookItem(userId, booking);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> acceptBooking(@PathVariable Long bookingId,
                                                @RequestParam(required = false) Boolean approved,
                                                @RequestHeader(value = SHARER_USER_ID, required = false) Long userId) {
        log.info("Accept booking {}, userId = {}, approved = {}", bookingId, userId, approved);
        return bookingClient.acceptBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@PathVariable Long bookingId,
                                             @RequestHeader(value = SHARER_USER_ID, required = false) Long userId) {
        log.info("Get booking {}, userId = {}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping()
    public ResponseEntity<Object> getBookerBookings(@RequestParam(defaultValue = "ALL") String state,
                                                    @RequestHeader(value = SHARER_USER_ID, required = false) Long userId,
                                                    @RequestParam(required = false) Long from,
                                                    @RequestParam(required = false) Long size) {
        log.info("Get booker bookings with state {}, userId = {}, from = {}, size = {}", state, userId, from, size);
        return bookingClient.getBookerBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnerBookings(@RequestParam(defaultValue = "ALL") String state,
                                                   @RequestHeader(value = SHARER_USER_ID, required = false) Long userId,
                                                   @RequestParam(required = false) Long from,
                                                   @RequestParam(required = false) Long size) {
        log.info("Get owner bookings with state {}, userId = {}, from = {}, size = {}", state, userId, from, size);
        return bookingClient.getOwnerBookings(userId, state, from, size);
    }
}



