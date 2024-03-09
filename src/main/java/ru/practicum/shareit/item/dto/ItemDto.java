package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.BookingInfo;
import ru.practicum.shareit.item.Comment;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class ItemDto {
    private int id;
    private String name;
    private String description;
    private Boolean available;
    private BookingInfo lastBooking;
    private BookingInfo nextBooking;
    private List<Comment> comments;

    public ItemDto(int id, String name, String description, Boolean available,
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
