package ru.practicum.shareit.request.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.request.ItemRequest;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestMapper {
    private final ItemMapper itemMapper;

    public ItemRequest convertToModel(ItemRequestDto obj) {
        List<ItemDto> itemDtos = obj.getItems();
        List<Item> items = itemDtos == null ? null : itemDtos.stream()
                .map(itemMapper::convertToModel).collect(Collectors.toList());
        return new ItemRequest(obj.getId(), obj.getItemId(), obj.getUserId(), obj.getName(),
                obj.getDescription(), obj.getCreated(), items);
    }

    public ItemRequestDto convertToDto(ItemRequest obj) {
        List<Item> items = obj.getItems();
        List<ItemDto> itemDtos = items == null ? null : items.stream()
                .map(itemMapper::convertToDto).collect(Collectors.toList());
        return new ItemRequestDto(obj.getId(), obj.getItemId(), obj.getUserId(), obj.getName(),
                obj.getDescription(), obj.getCreated(), itemDtos);
    }
}
