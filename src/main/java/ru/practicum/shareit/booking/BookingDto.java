package ru.practicum.shareit.booking;

import lombok.Data;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Data
public class BookingDto {
    private int id;

    private Item item;

    private User booker;

    private int itemId;

    private LocalDateTime start;

    private LocalDateTime end;

    private String feedback;

    private BookingStatus status;

    public BookingDto(int id, Item item, User booker, LocalDateTime start,
                      LocalDateTime end, String feedback, BookingStatus status) {
        this.id = id;
        this.item = item;
        this.booker = booker;
        this.start = start;
        this.end = end;
        this.feedback = feedback;
        this.status = status;
    }
}
