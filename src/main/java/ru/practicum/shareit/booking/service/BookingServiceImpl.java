package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.BookingStatusDto;
import ru.practicum.shareit.booking.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final BookingValidator bookingValidator;
    private final BookingMapper bookingMapper;
    private final ItemService itemService;
    private final UserService userService;
    private final PageValidation pageValidation;

    @Override
    public Booking findBooking(Integer bookingId) {
        if (bookingId == null) {
            throw new ValidationException("booking id can't be null");
        }
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("not found booking with id %d", bookingId)));
    }

    @Override
    public BookingDto createBooking(BookingDto bookingDto, Integer sharerId) {
        User booker = userService.findUser(sharerId);
        Item item = itemService.findItem(bookingDto.getItemId());

        bookingValidator.checkDates(bookingDto);

        Booking booking = bookingMapper.convertToModel(bookingDto);

        if (item.getOwner().getId().equals(sharerId)) {
            throw new NotFoundException("owner can't be booker");
        }

        if (!item.getAvailable()) {
            throw new ValidationException("item is not available");
        }

        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);
        booking.setItem(item);

        bookingRepository.save(booking);
        bookingRepository.flush();

        return bookingMapper.convertToDto(booking);
    }

    @Override
    public BookingDto acceptBooking(Integer bookingId, Integer userId, Boolean approved) {
        Booking booking = findBooking(bookingId);

        if (!booking.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Only owner can edit booking status");
        }
        if (booking.getStatus() == BookingStatus.APPROVED && approved) {
            throw new ValidationException("can't approve approved booking");
        }

        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        bookingRepository.save(booking);

        return bookingMapper.convertToDto(booking);
    }

    @Override
    public BookingDto getBooking(Integer bookingId, Integer userId) {
        Booking booking = findBooking(bookingId);

        if (!(booking.getOwner().getId().equals(userId) || booking.getBooker().getId().equals(userId))) {
            throw new NotFoundException("Only owner or booker cat get booking");
        }

        return bookingMapper.convertToDto(booking);
    }

    @Override
    public List<BookingDto> getBookerBookings(String status, Integer userId, Integer from, Integer size) {
        userService.findUser(userId);
        pageValidation.checkPage(from, size);
        BookingStatusDto state = bookingValidator.getStatus(status);
        return convertListToDto(getBookerBookingsState(state, userId, from, size));
    }

    @Override
    public List<BookingDto> getOwnerBookings(String status, Integer userId, Integer from, Integer size) {
        userService.findUser(userId);
        pageValidation.checkPage(from, size);
        BookingStatusDto state = bookingValidator.getStatus(status);
        return convertListToDto(getOwnerBookingsState(state, userId, from, size));
    }

    private List<BookingDto> convertListToDto(List<Booking> bookings) {
        return bookings.stream().map(bookingMapper::convertToDto).collect(Collectors.toList());
    }

    private List<Booking> getBookerBookingsState(BookingStatusDto state, Integer userId, Integer from, Integer size) {
        LocalDateTime now = LocalDateTime.now();

        switch (state) {
            case ALL:
                if (from != null || size != null) {
//                    Pageable pageable = PageRequest.of((int) from / size, size, Sort.by("start").descending());
//                    return bookingRepository.findAll(pageable).stream().filter(booking -> booking.getBooker().getId().equals(userId)).collect(Collectors.toList());
                    List<Booking> bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
                    return bookings.subList(from, Math.min(from + size, bookings.size()));
                } else {
                    return bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
                }

            case CURRENT:
                return bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, now, now);
            case PAST:
                return bookingRepository.findAllByBookerIdAndStatusAndEndBeforeOrderByStartDesc(userId, BookingStatus.APPROVED, now);
            case FUTURE:
                return bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId, now);
            case REJECTED:
                return bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED);
            case WAITING:
                return bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING);
        }
        return Collections.emptyList();
    }

    private List<Booking> getOwnerBookingsState(BookingStatusDto state, Integer userId, Integer from, Integer size) {
        LocalDateTime now = LocalDateTime.now();

        switch (state) {
            case ALL:
                if (from != null && size != null) {
                    List<Booking> bookings = bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId);
                    return bookings.subList(from, Math.min(from + size, bookings.size()));
                } else {
                    return bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId);
                }
            case CURRENT:
                return bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, now, now);
            case PAST:
                return bookingRepository.findAllByItemOwnerIdAndStatusAndEndBeforeOrderByStartDesc(userId, BookingStatus.APPROVED, now);
            case FUTURE:
                return bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(userId, now);
            case REJECTED:
                return bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED);
            case WAITING:
                return bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING);
        }
        return Collections.emptyList();
    }
}
