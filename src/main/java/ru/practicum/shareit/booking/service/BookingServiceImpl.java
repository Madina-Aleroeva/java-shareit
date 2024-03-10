package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.BookingStatusDto;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.BookingValidator;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingValidator bookingValidator;
    private final BookingMapper bookingMapper;

    @Override
    public BookingDto createBooking(BookingDto bookingDto, Integer sharerId) {
        bookingValidator.checkSharerId(sharerId);
        bookingValidator.checkItemId(bookingDto.getItemId());
        bookingValidator.checkDates(bookingDto);

        Booking booking = bookingMapper.convertToModel(bookingDto);

        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("not found item"));

        if (item.getOwner().getId().equals(sharerId)) {
            throw new NotFoundException("owner can't be booker");
        }

        User booker = userRepository.findById(sharerId)
                .orElseThrow(() -> new NotFoundException("not found booker"));
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);
        booking.setItem(item);


        bookingRepository.save(booking);
        bookingRepository.flush();

        return bookingMapper.convertToDto(booking);
    }

    @Override
    public BookingDto acceptBooking(Integer bookingId, Integer userId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("not found booking"));

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
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("not found booking"));

        if (!(booking.getOwner().getId().equals(userId) || booking.getBooker().getId().equals(userId))) {
            throw new NotFoundException("Only owner or booker cat get booking");
        }

        return bookingMapper.convertToDto(booking);
    }

    @Override
    public List<BookingDto> getBookerBookings(String status, Integer userId) {
        bookingValidator.checkSharerId(userId);
        BookingStatusDto state = bookingValidator.getStatus(status);
        return convertListToDto(getBookerBookingsState(state, userId));
    }

    @Override
    public List<BookingDto> getOwnerBookings(String status, Integer userId) {
        bookingValidator.checkSharerId(userId);
        BookingStatusDto state = bookingValidator.getStatus(status);
        return convertListToDto(getOwnerBookingsState(state, userId));
    }

    private List<BookingDto> convertListToDto(List<Booking> bookings) {
        return bookings.stream().map(bookingMapper::convertToDto).collect(Collectors.toList());
    }

    private List<Booking> getBookerBookingsState(BookingStatusDto state, Integer userId) {
        LocalDateTime now = LocalDateTime.now();

        switch (state) {
            case ALL:
                return bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
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

    private List<Booking> getOwnerBookingsState(BookingStatusDto state, Integer userId) {
        LocalDateTime now = LocalDateTime.now();

        switch (state) {
            case ALL:
                return bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId);
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
