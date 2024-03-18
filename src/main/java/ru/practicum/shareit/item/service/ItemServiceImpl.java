package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingInfo;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemValidator itemValidator;
    private final CommentMapper commentMapper;
    private final UserService userService;

    @Override
    public Item findItem(Integer itemId) {
        if (itemId == null) {
            throw new ValidationException("item id can't be null");
        }

        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("not found item with id %d", itemId)));
    }

    @Override
    public List<ItemDto> getOwnerItems(Integer sharerId) {
        List<Booking> bookings = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStart(sharerId, BookingStatus.APPROVED);

        return itemRepository.findAllByOwnerId(sharerId).stream()
                .map(itemMapper::convertToDto)
                .map(itemDto -> addBookings(itemDto, itemDto.getId(), bookings))
                .collect(Collectors.toList());
    }

    private ItemDto addBookings(ItemDto itemDto, Integer itemId, List<Booking> allBookings) {

        LocalDateTime now = LocalDateTime.now();

        List<Booking> bookings = allBookings.stream().filter(booking -> booking.getItem().getId().equals(itemId)).collect(Collectors.toList());

        for (Booking booking : bookings) {
            if (booking.getStart().isBefore(now)) {
                itemDto.setLastBooking(new BookingInfo(booking));
            }
            if (booking.getStart().isAfter(now)) {
                itemDto.setNextBooking(new BookingInfo(booking));
                break;
            }
        }
        return itemDto;
    }

    @Override
    public ItemDto getItem(Integer itemId, Integer sharerId) {
        Item item = findItem(itemId);
        ItemDto itemDto = itemMapper.convertToDto(item);
        List<Booking> bookings = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStart(sharerId, BookingStatus.APPROVED);
        addBookings(itemDto, itemId, bookings);
        return itemDto;
    }

    @Override
    public ItemDto addItem(ItemDto itemDto, Integer sharerId) {
        Item item = itemMapper.convertToModel(itemDto);
        itemValidator.checkItemFields(item);
        item.setOwner(userService.findUser(sharerId));
        return itemMapper.convertToDto(itemRepository.save(item));
    }

    @Override
    public CommentDto addComment(CommentDto commentDto, Integer sharerId, Integer itemId) {
        Comment comment = commentMapper.convertToModel(commentDto);
        if (comment.getText().isEmpty()) {
            throw new ValidationException("text is empty");
        }
        Item item = findItem(itemId);

        List<Booking> bookings = bookingRepository.findAllByBookerIdAndItemIdAndStatusAndStartBefore(sharerId, itemId, BookingStatus.APPROVED, LocalDateTime.now());
        if (bookings.isEmpty()) {
            throw new ValidationException("not found such booking");
        }

        comment.setCreated(LocalDateTime.now());
        comment.setAuthorName(userService.findUser(sharerId).getName());
        comment.setItem(item);

        commentRepository.save(comment);
        commentRepository.flush();

        return commentMapper.convertToDto(comment);
    }

    private Item editItem(Integer id, Item editItem) {
        Item curItem = findItem(id);

        extractMethod(editItem, curItem);

        itemRepository.save(curItem);
        return curItem;
    }

    public static void extractMethod(Item editItem, Item curItem) {
        if (editItem.getName() != null) {
            curItem.setName(editItem.getName());
        }
        if (editItem.getDescription() != null) {
            curItem.setDescription(editItem.getDescription());
        }
        if (editItem.getAvailable() != null) {
            curItem.setAvailable(editItem.getAvailable());
        }
    }

    @Override
    public ItemDto editItem(Integer itemId, ItemDto itemDto, Integer sharerId) {
        userService.findUser(sharerId);
        Item existingItem = findItem(itemId);
        Item newItem = itemMapper.convertToModel(itemDto);
        if (!existingItem.getOwner().getId().equals(sharerId)) {
            throw new NotFoundException("sharer id is not the owners");
        }
        return itemMapper.convertToDto(editItem(itemId, newItem));
    }

    @Override
    public List<ItemDto> search(String text) {
        return searchStr(text).stream().map(itemMapper::convertToDto).collect(Collectors.toList());
    }

    public List<Item> searchStr(String search) {
        if (search.isBlank())
            return Collections.emptyList();
        return itemRepository.findAllByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCase(search, search)
                .stream().filter(Item::getAvailable).collect(Collectors.toList());
    }
}
