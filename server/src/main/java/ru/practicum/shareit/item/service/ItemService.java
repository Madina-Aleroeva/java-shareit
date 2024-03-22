package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.CommentDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    Item findItem(Long itemId);

    List<ItemDto> getOwnerItems(Long userId);

    ItemDto getItem(Long itemId, Long sharerId);

    ItemDto addItem(ItemDto itemDto, Long sharerId);

    CommentDto addComment(CommentDto comment, Long sharerId, Long itemId);

    ItemDto editItem(Long id, ItemDto itemDto, Long sharerId);

    List<ItemDto> search(String text);
}
