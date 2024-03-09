package ru.practicum.shareit.item;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import ru.practicum.shareit.booking.BookingInfo;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Entity
@Table(name = "items")
public class Item {
    @Id
    @SequenceGenerator(name = "items_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "items_seq")
    private int id;

    @ManyToOne
    private User owner;

    @OneToOne
    private BookingInfo lastBooking;

    @OneToOne
    private BookingInfo nextBooking;

    private String name;

    private String description;

    @OneToMany(mappedBy = "item")
    @JsonManagedReference
    private List<Comment> comments;

    private Boolean available;

    public Item() {
    }

    public Item(int id, String name, String description, Boolean available,
                BookingInfo lastBooking, BookingInfo nextBooking, List<Comment> comments) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.lastBooking = lastBooking;
        this.nextBooking = nextBooking;
        this.comments = comments;
    }
}
