package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.BookingStatusDto;
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
    private final BookingMapper bookingMapper;
    private final ItemService itemService;
    private final UserService userService;

    @Override
    public Booking findBooking(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("not found booking with id %d", bookingId)));
    }

    @Override
    public BookingDto createBooking(BookingDto bookingDto, Long sharerId) {
        User booker = userService.findUser(sharerId);
        Item item = itemService.findItem(bookingDto.getItemId());

        Booking booking = bookingMapper.convertToModel(bookingDto);

        if (item.getOwner().getId().equals(sharerId)) {
            throw new NotFoundException("owner can't be booker");
        }

        // TODO: gateway
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
    public BookingDto acceptBooking(Long bookingId, Long userId, Boolean approved) {
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
    public BookingDto getBooking(Long bookingId, Long userId) {
        Booking booking = findBooking(bookingId);

        if (!(booking.getOwner().getId().equals(userId) || booking.getBooker().getId().equals(userId))) {
            throw new NotFoundException("Only owner or booker cat get booking");
        }

        return bookingMapper.convertToDto(booking);
    }

    @Override
    public List<BookingDto> getBookerBookings(String status, Long userId, Long from, Long size) {
        userService.findUser(userId);
        BookingStatusDto state = BookingStatusDto.valueOf(status);
        return convertListToDto(getBookerBookingsState(state, userId, from, size));
    }

    @Override
    public List<BookingDto> getOwnerBookings(String status, Long userId, Long from, Long size) {
        userService.findUser(userId);
        BookingStatusDto state = BookingStatusDto.valueOf(status);
        return convertListToDto(getOwnerBookingsState(state, userId, from, size));
    }

    private List<BookingDto> convertListToDto(List<Booking> bookings) {
        return bookings.stream().map(bookingMapper::convertToDto).collect(Collectors.toList());
    }

    private List<Booking> getBookerBookingsState(BookingStatusDto state, Long userId, Long from, Long size) {
        LocalDateTime now = LocalDateTime.now();

        switch (state) {
            case ALL:
                // TODO: extract
                if (from != null || size != null) {
                    List<Booking> bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
                    return bookings.subList(from.intValue(), Math.min(from.intValue() + size.intValue(), bookings.size()));
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

    private List<Booking> getOwnerBookingsState(BookingStatusDto state, Long userId, Long from, Long size) {
        LocalDateTime now = LocalDateTime.now();

        switch (state) {
            case ALL:
                if (from != null && size != null) {
                    List<Booking> bookings = bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId);
                    return bookings.subList(from.intValue(), Math.min(from.intValue() + size.intValue(), bookings.size()));
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
