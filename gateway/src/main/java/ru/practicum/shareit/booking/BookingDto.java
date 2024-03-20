package ru.practicum.shareit.booking;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserDto;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingDto {
    private Long id;

    private ItemDto item;

    private UserDto booker;

    private Long itemId;

    private BookingStatus status;

    private LocalDateTime start;

    private LocalDateTime end;
}
