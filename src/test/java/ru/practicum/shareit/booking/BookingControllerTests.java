package ru.practicum.shareit.booking;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.shareit.booking.BookingStatus.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookingControllerTests {
    @Autowired
    private BookingController bookingController;

    @Autowired
    private UserController userController;

    @Autowired
    private ItemController itemController;

    private ItemDto itemDto;

    private UserDto userDto;

    private UserDto userDto1;

    private BookingDto bookingDto;

    @BeforeEach
    void init() {
        itemDto = ItemDto
                .builder()
                .name("name")
                .description("description")
                .available(true)
                .comments(Collections.emptyList())
                .build();

        userDto = UserDto
                .builder()
                .name("name")
                .email("user@email.com")
                .build();

        userDto1 = UserDto
                .builder()
                .name("name")
                .email("user1@email.com")
                .build();

        bookingDto = BookingDto
                .builder()
                .start(LocalDateTime.of(2024, 10, 24, 12, 30))
                .end(LocalDateTime.of(2024, 11, 10, 13, 0))
                .itemId(1).build();
    }

    @Test
    void createTest() {
        UserDto user = userController.addUser(userDto);
        ItemDto item = itemController.addItem(itemDto, user.getId());
        UserDto user1 = userController.addUser(userDto1);
        BookingDto booking = bookingController.createBooking(bookingDto, user1.getId());

        assertEquals(1, bookingController.getBooking(booking.getId(), user1.getId()).getId());
    }

    @Test
    void createByWrongUserTest() {
        assertThrows(NotFoundException.class, () -> bookingController.createBooking(bookingDto, 1));
    }

    @Test
    void createForWrongItemTest() {
        UserDto user = userController.addUser(userDto);
        assertThrows(NotFoundException.class, () -> bookingController.createBooking(bookingDto, 1));
    }

    @Test
    void createByOwnerTest() {
        UserDto user = userController.addUser(userDto);
        ItemDto item = itemController.addItem(itemDto, user.getId());
        assertThrows(NotFoundException.class, () -> bookingController.createBooking(bookingDto, 1));
    }

    @Test
    void createToUnavailableItemTest() {
        UserDto user = userController.addUser(userDto);
        itemDto.setAvailable(false);
        ItemDto item = itemController.addItem(itemDto, user.getId());
        UserDto user1 = userController.addUser(userDto1);
        assertThrows(ValidationException.class, () -> bookingController.createBooking(bookingDto, 2));
    }

    @Test
    void approveTest() {
        UserDto user = userController.addUser(userDto);
        ItemDto item = itemController.addItem(itemDto, user.getId());
        UserDto user1 = userController.addUser(userDto1);
        BookingDto booking = bookingController.createBooking(BookingDto
                .builder()
                .start(LocalDateTime.of(2024, 10, 24, 12, 30))
                .end(LocalDateTime.of(2024, 11, 10, 13, 0))
                .itemId(item.getId()).build(), user1.getId());

        assertEquals(WAITING, bookingController.getBooking(booking.getId(), user1.getId()).getStatus());
        bookingController.acceptBooking(booking.getId(), true, user.getId());
        assertEquals(APPROVED, bookingController.getBooking(booking.getId(), user1.getId()).getStatus());
    }

    @Test
    void approveToWrongBookingTest() {
        assertThrows(NotFoundException.class, () -> bookingController.acceptBooking(1, true, 1));
    }

    @Test
    void approveByWrongUserTest() {
        UserDto user = userController.addUser(userDto);
        ItemDto item = itemController.addItem(itemDto, user.getId());
        UserDto user1 = userController.addUser(userDto1);
        BookingDto booking = bookingController.createBooking(bookingDto, user1.getId());

        assertThrows(NotFoundException.class, () -> bookingController.acceptBooking(1, true, 2));
    }

    @Test
    void approveToBookingWithWrongStatus() {
        UserDto user = userController.addUser(userDto);
        ItemDto item = itemController.addItem(itemDto, user.getId());
        UserDto user1 = userController.addUser(userDto1);
        BookingDto booking = bookingController.createBooking(bookingDto, user1.getId());

        bookingController.acceptBooking(1, true, 1);
        assertThrows(ValidationException.class, () -> bookingController.acceptBooking(1, true, 1));
    }

    @Test
    void getAllByUserTest() {
        UserDto user = userController.addUser(userDto);
        ItemDto item = itemController.addItem(itemDto, user.getId());
        UserDto user1 = userController.addUser(userDto1);
        BookingDto booking = bookingController.createBooking(bookingDto, user1.getId());

        assertEquals(1, bookingController.getBookerBookings("WAITING", user1.getId(), 0, 10).size());
        assertEquals(1, bookingController.getBookerBookings("ALL", user1.getId(), 0, 10).size());
        assertEquals(0, bookingController.getBookerBookings("PAST", user1.getId(), 0, 10).size());
        assertEquals(0, bookingController.getBookerBookings("CURRENT", user1.getId(), 0, 10).size());
        assertEquals(1, bookingController.getBookerBookings("FUTURE", user1.getId(), 0, 10).size());
        assertEquals(0, bookingController.getBookerBookings("REJECTED", user1.getId(), 0, 10).size());

        bookingController.acceptBooking(booking.getId(), true, user.getId());

        assertEquals(0, bookingController.getOwnerBookings("CURRENT", user.getId(), 0, 10).size());
        assertEquals(1, bookingController.getOwnerBookings("ALL", user.getId(), 0, 10).size());
        assertEquals(0, bookingController.getOwnerBookings("WAITING", user.getId(), 0, 10).size());
        assertEquals(1, bookingController.getOwnerBookings("FUTURE", user.getId(), 0, 10).size());
        assertEquals(0, bookingController.getOwnerBookings("REJECTED", user.getId(), 0, 10).size());
        assertEquals(0, bookingController.getOwnerBookings("PAST", user.getId(), 0, 10).size());
    }

    @Test
    void getAllByWrongUserTest() {
        assertThrows(NotFoundException.class, () -> bookingController.getBookerBookings("ALL", 1, 0, 10));
        assertThrows(NotFoundException.class, () -> bookingController.getOwnerBookings("ALL", 1, 0, 10));
    }

    @Test
    void getByWrongIdTest() {
        assertThrows(NotFoundException.class, () -> bookingController.getBooking(1, 1));
    }

    @Test
    void getByWrongUser() {
        UserDto user = userController.addUser(userDto);
        ItemDto item = itemController.addItem(itemDto, user.getId());
        UserDto user1 = userController.addUser(userDto1);
        BookingDto booking = bookingController.createBooking(bookingDto, user1.getId());

        assertThrows(NotFoundException.class, () -> bookingController.getBooking(1, 10));
    }
}
