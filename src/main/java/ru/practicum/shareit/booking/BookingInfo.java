package ru.practicum.shareit.booking;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Table(name = "bookings_info")
@Entity
@NoArgsConstructor
public class BookingInfo {
    @Id
    private int id;

    private Integer bookerId;

    @Column(name = "start_date")
    private LocalDateTime start;

    @Column(name = "end_date")
    private LocalDateTime end;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    public BookingInfo(Booking booking) {
        this.id = booking.getId();
        this.bookerId = booking.getBooker() == null ? null : booking.getBooker().getId();
        this.start = booking.getStart();
        this.end = booking.getEnd();
        this.status = booking.getStatus();
    }
}
