package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

@Service
@RequiredArgsConstructor
public class BookingMapper {
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;

    public Booking convertToModel(BookingDto obj) {
        ItemDto itemDto = obj.getItem();
        Item item = itemDto == null ? null : itemMapper.convertToModel(itemDto);

        UserDto bookerDto = obj.getBooker();
        User booker = bookerDto == null ? null : userMapper.convertToUser(bookerDto);

        return new Booking(obj.getId(), item, booker,
                obj.getStatus(), obj.getStart(), obj.getEnd());
    }

    public BookingDto convertToDto(Booking obj) {
        Item item = obj.getItem();
        ItemDto itemDto = item == null ? null : itemMapper.convertToDto(item);

        User booker = obj.getBooker();
        UserDto bookerDto = booker == null ? null : userMapper.convertToDto(booker);

        return BookingDto.builder()
                .id(obj.getId())
                .item(itemDto)
                .booker(bookerDto)
                .status(obj.getStatus())
                .start(obj.getStart())
                .end(obj.getEnd())
                .build();
    }
}
