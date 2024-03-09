package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingInfo;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.CommentRepository;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public List<ItemDto> getOwnerItems(int sharerId) {
        List<Item> items = itemRepository.findAllByOwnerId(sharerId);
        for (Item item : items) {
            addBookings(item, item.getId(), sharerId);
        }
        return items.stream()
                .map(itemMapper::convertToDto)
                .collect(Collectors.toList());
    }

    private void addBookings(Item item, int itemId, int sharerId) {

        LocalDateTime now = LocalDateTime.now();

        List<Booking> bookings = bookingRepository.findAllByItemIdAndItemOwnerIdAndStatusOrderByStart(itemId, sharerId, BookingStatus.APPROVED);

        for (Booking booking : bookings) {
            if (booking.getStart().isBefore(now)) {
                item.setLastBooking(new BookingInfo(booking));
            }
            if (booking.getStart().isAfter(now)) {
                item.setNextBooking(new BookingInfo(booking));
                break;
            }
        }
    }

    @Override
    public ItemDto getItem(int itemId, int sharerId) {
        Optional<Item> itemOptional = itemRepository.findById(itemId);
        if (itemOptional.isEmpty()) {
            throw new NotFoundException("not found item");
        }
        Item item = itemOptional.get();

        addBookings(item, itemId, sharerId);

        return itemMapper.convertToDto(item);
    }

    @Override
    public ItemDto addItem(ItemDto itemDto, Integer sharerId) {
        Item item = itemMapper.convertToModel(itemDto);
        checkItemFields(item);
        checkSharerId(sharerId);
        item.setOwner(userRepository.findById(sharerId).get());
        return itemMapper.convertToDto(itemRepository.save(item));
    }

    @Override
    public Comment addComment(Comment comment, Integer sharerId, Integer itemId) {
        if (comment.getText().isEmpty()) {
            throw new ValidationException("text is empty");
        }
        Optional<Item> itemOptional = itemRepository.findById(itemId);
        if (itemOptional.isEmpty()) {
            throw new NotFoundException("not found item to add comment");
        }
        Item item = itemOptional.get();

        List<Booking> bookings = bookingRepository.findAllByBookerIdAndItemIdAndStatusAndStartBefore(sharerId, itemId, BookingStatus.APPROVED, LocalDateTime.now());
        System.out.println("add comment from " + sharerId + " to item " + itemId);
        System.out.println(bookings);
        if (bookings.isEmpty()) {
            throw new ValidationException("not found such booking");
        }

        comment.setCreated(LocalDateTime.now());
        comment.setAuthorName(userRepository.findById(sharerId).get().getName());
        comment.setItem(item);

        commentRepository.save(comment);
        commentRepository.flush();

        return comment;
    }

    private Item editItem(int id, Item editItem) {
        Item curItem = itemRepository.findById(id).get();

        if (editItem.getName() != null) {
            curItem.setName(editItem.getName());
        }
        if (editItem.getDescription() != null) {
            curItem.setDescription(editItem.getDescription());
        }
        if (editItem.getAvailable() != null) {
            curItem.setAvailable(editItem.getAvailable());
        }

        itemRepository.save(curItem);
        return curItem;
    }

    @Override
    public ItemDto editItem(int itemId, ItemDto itemDto, Integer sharerId) {
        checkSharerId(sharerId);
        Item existingItem = itemRepository.findById(itemId).get();
        Item newItem = itemMapper.convertToModel(itemDto);
        if (existingItem.getOwner().getId() != sharerId) {
            throw new NotFoundException("sharer id is not the owners");
        }
        return itemMapper.convertToDto(editItem(itemId, newItem));
    }

    @Override
    public void delItem(int itemId) {
        itemRepository.deleteById(itemId);
    }

    private boolean equals(String search, Item item) {
        if (!item.getAvailable())
            return false;
        return hasOccurrence(search, item.getName()) || hasOccurrence(search, item.getDescription());
    }

    private boolean hasOccurrence(String search, String phrases) {
        List<String> names = Stream.of(phrases.split(" "))
                .map(s -> s.toLowerCase().trim()).collect(Collectors.toList());

        for (String name : names) {
            if (name.equals(search) || name.startsWith(search) || name.endsWith(search))
                return true;
        }
        return false;
    }

    private List<Item> searchStr(String search) {
        String finalSearch = search.toLowerCase().trim();
        if (search.isBlank())
            return Collections.emptyList();
        return itemRepository.findAll().stream().filter(item -> equals(finalSearch, item)).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(String text) {
        return searchStr(text).stream().map(itemMapper::convertToDto).collect(Collectors.toList());
    }

    private void checkSharerId(Integer sharerId) {
        if (sharerId == null) {
            throw new RuntimeException("sharer user id is empty");
        }
        if (userRepository.findById(sharerId).isEmpty()) {
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
