package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public List<ItemDto> getOwnerItems(int userId) {
        return itemRepository.getAllItems(userId).stream().map(ItemMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public ItemDto getItem(int itemId) {
        return ItemMapper.convertToDto(itemRepository.getItem(itemId));
    }

    @Override
    public ItemDto addItem(ItemDto itemDto, Integer sharerId) {
        checkSharerId(sharerId);
        Item item = ItemMapper.convertToModel(itemDto);
        checkItemFields(item);
        item.setOwner(userRepository.getUser(sharerId));
        return ItemMapper.convertToDto(itemRepository.addItem(item));
    }

    @Override
    public ItemDto editItem(int itemId, ItemDto itemDto, Integer sharerId) {
        checkSharerId(sharerId);
        Item existingItem = itemRepository.getItem(itemId);
        Item newItem = ItemMapper.convertToModel(itemDto);
        if (existingItem.getOwner().getId() != sharerId) {
            throw new NotFoundException("sharer id is not the owners");
        }
        return ItemMapper.convertToDto(itemRepository.editItem(itemId, newItem));
    }

    @Override
    public void delItem(int itemId) {
        itemRepository.delItem(itemId);
    }

    @Override
    public List<ItemDto> search(String text) {
        return itemRepository.search(text).stream().map(ItemMapper::convertToDto).collect(Collectors.toList());
    }

    private void checkSharerId(Integer sharerId) {
        if (sharerId == null) {
            throw new RuntimeException("sharer user id is empty");
        }
        if (!userRepository.containsUser(sharerId)) {
            throw new NotFoundException("sharer user id doesn't exist");
        }
    }

    private void checkItemFields(Item item) {
        if (item.getName() == null || item.getName().isBlank()) {
            throw new ValidationException("Item should have name");
        }
        if (item.getDescription() == null || item.getDescription().isBlank()) {
            throw new ValidationException("Item should have description");
        }
        if (item.getAvailable() == null) {
            throw new ValidationException("Item should have available");
        }
    }
}
