package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.BookingStatusDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingValidator bookingValidator;
    private final BookingInfoRepository bookingInfoRepository;

    public Booking createBooking(BookingDto bookingDto, int sharerId) {
        bookingValidator.checkSharerId(sharerId);
        bookingValidator.checkItemId(bookingDto.getItemId());
        bookingValidator.checkDates(bookingDto);

        Booking booking = BookingMapper.convertToModel(bookingDto);

        Item item = itemRepository.findById(bookingDto.getItemId()).get();
        if (item.getOwner().getId() == sharerId) {
            throw new NotFoundException("owner can't be booker");
        }

        bookingRepository.save(booking);
        bookingRepository.flush();

        booking.setStatus(BookingStatus.WAITING);
        booking.setItem(item);
        booking.setBooker(userRepository.findById(sharerId).get());

        LocalDateTime now = LocalDateTime.now();

        /*if (booking.getEnd().isBefore(now)){
            BookingInfo bookingInfo = new BookingInfo(booking);
            bookingInfoRepository.save(bookingInfo);
            item.setLastBooking(bookingInfo);
        }
        if (booking.getStart().isAfter(now)){
            BookingInfo bookingInfo = new BookingInfo(booking);
            bookingInfoRepository.save(bookingInfo);
            item.setNextBooking(bookingInfo);
        }*/


        bookingRepository.save(booking);
        bookingRepository.flush();

        return booking;
    }

    public Booking acceptBooking(int bookingId, int userId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).get();
        if (booking.getItem().getOwner().getId() != userId) {
            throw new NotFoundException("Only owner can edit booking status");
        }
        if (booking.getStatus() == BookingStatus.APPROVED && approved) {
            throw new ValidationException("can't approve approved booking");
        }

        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        bookingRepository.save(booking);

        return booking;
    }

    public Booking getBooking(int bookingId, int userId) {
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
        if (bookingOptional.isEmpty()) {
            throw new NotFoundException("booking id not exists");
        }
        Booking booking = bookingOptional.get();
        if (!(booking.getOwner().getId() == userId || booking.getBooker().getId() == userId)) {
            throw new NotFoundException("Only owner or booker cat get booking");
        }

        return booking;
    }

    private BookingStatusDto getStatus(String status) {
        try {
            return BookingStatusDto.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Unknown state: " + status);
        }
    }

//    private List<Booking> find

    public List<Booking> getBookerBookings(String status, int userId) {
        bookingValidator.checkSharerId(userId);

        BookingStatusDto state = getStatus(status);
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
        return null;
    }

    public List<Booking> getOwnerBookings(String status, int userId) {
        bookingValidator.checkSharerId(userId);

        BookingStatusDto state = getStatus(status);
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
        return null;
    }
}
