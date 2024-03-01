package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    List<ItemDto> getOwnerItems(int userId);

    ItemDto getItem(int id);

    ItemDto addItem(ItemDto itemDto, Integer sharerId);

    ItemDto editItem(int id, ItemDto itemDto, Integer sharerId);

    void delItem(int id);

    List<ItemDto> search(String text);
}