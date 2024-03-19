package ru.practicum.shareit.booking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
public class BookingRepositoryTests {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private User user;

    private Item item;

    private User user2;

    private Booking booking;

    @BeforeEach
    void init() {
        user = User.builder()
                .name("name")
                .email("email@email.com")
                .build();

        item = Item.builder()
                .name("name")
                .description("description")
                .available(true)
                .owner(user)
                .build();

        user2 = User.builder()
                .name("name2")
                .email("email2@email.com")
                .build();

        booking = Booking.builder()
                .start(LocalDateTime.of(2025, 1, 10, 10, 30))
                .end(LocalDateTime.of(2025, 2, 10, 10, 30))
                .item(item)
                .booker(user2)
                .status(BookingStatus.APPROVED)
                .build();
    }

    @AfterEach
    void tearDown() {
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findAllByBookerIdAndItemIdAndStatusAndStartBeforeTest() {
        userRepository.save(user);
        itemRepository.save(item);
        userRepository.save(user2);
        bookingRepository.save(booking);

        assertThat(bookingRepository.findAllByBookerIdAndItemIdAndStatusAndStartBefore(user2.getId(), item.getId(),
                                BookingStatus.APPROVED,
                                LocalDateTime.of(2025, 10, 9, 10, 30)
                        )
                        .get(0)
                        .getStart(),
                equalTo(LocalDateTime.of(2025, 1, 10, 10, 30)));
    }

    @Test
    void findAllByBookerTest() {
        userRepository.save(user);
        itemRepository.save(item);
        userRepository.save(user2);
        bookingRepository.save(booking);

        assertThat(bookingRepository.findAllByBookerIdOrderByStartDesc(user2.getId()).size(), equalTo(1));
    }

    @Test
    void findAllByBookerIdAndItemIdAndStatusEqualsAndEndIsBeforeTest() {
        userRepository.save(user);
        itemRepository.save(item);
        userRepository.save(user2);
        booking.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(booking);

        assertThat(bookingRepository.findAllByBookerIdAndStatusAndEndBeforeOrderByStartDesc(user2.getId(), BookingStatus.APPROVED,
                        LocalDateTime.of(2025, 5, 10, 10, 10)).size(),
                equalTo(1));
    }

    @Test
    void findAllByBookerAndStartBeforeAndEndAfterTest() {
        userRepository.save(user);
        itemRepository.save(item);
        userRepository.save(user2);
        bookingRepository.save(booking);

        assertThat(bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(user2.getId(),
                LocalDateTime.of(2025, 3, 1, 10, 10),
                LocalDateTime.of(2024, 12, 1, 10, 30)
        ).size(), equalTo(1));
    }

    @Test
    void findAllByItemOwnerAndEndBeforeTest() {
        userRepository.save(user);
        itemRepository.save(item);
        userRepository.save(user2);
        bookingRepository.save(booking);

        assertThat(bookingRepository.findAllByItemOwnerIdAndStatusAndEndBeforeOrderByStartDesc(user.getId(),
                BookingStatus.APPROVED,
                LocalDateTime.of(2025, 4, 10, 10, 10)).size(), equalTo(1));
    }

    @Test
    void findAllByItemOwnerAndStartAfterTest() {
        userRepository.save(user);
        itemRepository.save(item);
        userRepository.save(user2);
        bookingRepository.save(booking);

        assertThat(bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(user.getId(),
                LocalDateTime.now()).size(), equalTo(1));
    }

    @Test
    void findAllByItemOwnerAndStatusEqualsTest() {
        userRepository.save(user);
        itemRepository.save(item);
        userRepository.save(user2);
        bookingRepository.save(booking);

        assertThat(bookingRepository.findAllByItemOwnerIdAndStatusOrderByStart(user.getId(), BookingStatus.APPROVED)
                .size(), equalTo(1));
    }

}
