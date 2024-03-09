package ru.practicum.shareit.booking;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */

@Data
@Table(name = "bookings")
@Entity
@NoArgsConstructor
public class Booking {
    @Id
    @SequenceGenerator(name = "booking_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "booking_seq")
    private int id;

    @ManyToOne
    private Item item;

    @ManyToOne
    private User booker;

    @Column(name = "start_date")
    private LocalDateTime start;

    @Column(name = "end_date")
    private LocalDateTime end;

    private String feedback;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    public Booking(int id, Item item, User booker,
                   LocalDateTime start, LocalDateTime end,
                   String feedback, BookingStatus status) {
        this.id = id;
        this.item = item;
        this.booker = booker;
        this.start = start;
        this.end = end;
        this.feedback = feedback;
        this.status = status;
    }

    public User getOwner() {
        if (item == null)
            return null;
        return item.getOwner();
    }
}
