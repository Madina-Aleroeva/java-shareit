package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.Item;

@Service
public class ItemMapper {

    public Item convertToModel(ItemDto itemDto) {
        return new Item(itemDto.getId(), itemDto.getName(), itemDto.getDescription(),
                itemDto.getAvailable(), itemDto.getLastBooking(), itemDto.getNextBooking(),
                itemDto.getComments());
    }

    public ItemDto convertToDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(),
                item.getAvailable(), item.getLastBooking(), item.getNextBooking(),
                item.getComments());
    }

}


