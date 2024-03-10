package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Data
public class BookingDto {
    private Integer id;

    private ItemDto item;

    private UserDto booker;

    private int itemId;

    private BookingStatus status;

    private LocalDateTime start;

    private LocalDateTime end;

    public BookingDto(Integer id, ItemDto itemDto, UserDto booker, BookingStatus status,
                      LocalDateTime start, LocalDateTime end) {
        this.id = id;
        this.item = itemDto;
        this.booker = booker;
        this.status = status;
        this.start = start;
        this.end = end;
    }
}
