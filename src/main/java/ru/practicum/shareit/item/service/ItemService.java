package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.CommentDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    Item findItem(Integer itemId);

    List<ItemDto> getOwnerItems(Integer userId);

    ItemDto getItem(Integer itemId, Integer sharerId);

    ItemDto addItem(ItemDto itemDto, Integer sharerId);

    CommentDto addComment(CommentDto comment, Integer sharerId, Integer itemId);

    ItemDto editItem(Integer id, ItemDto itemDto, Integer sharerId);

    List<ItemDto> search(String text);
}
