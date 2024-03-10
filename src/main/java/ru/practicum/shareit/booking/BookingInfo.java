package ru.practicum.shareit.booking;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class BookingInfo {
    private int id;
    private Integer bookerId;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingStatus status;

    // create booking info using booking entity
    public BookingInfo(Booking booking) {
        this.id = booking.getId();
        this.bookerId = booking.getBooker() == null ? null : booking.getBooker().getId();
        this.start = booking.getStart();
        this.end = booking.getEnd();
        this.status = booking.getStatus();
    }
}
