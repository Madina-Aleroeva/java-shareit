package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemControllerTests {
    @Autowired
    private ItemController itemController;

    @Autowired
    private UserController userController;

    @Autowired
    private BookingController bookingController;

    @Autowired
    private ItemRequestController itemRequestController;

    private ItemDto itemDto;

    private ItemDto itemDto2;

    private UserDto userDto;

    private UserDto userDto2;

    private ItemRequestDto itemRequestDto;

    private CommentDto comment;

    @BeforeEach
    void init() {
        itemDto = ItemDto
                .builder()
                .name("name")
                .description("description")
                .available(true)
                .build();

        itemDto2 = ItemDto
                .builder()
                .name("new name")
                .description("new description")
                .available(true)
                .build();

        userDto = UserDto
                .builder()
                .name("name")
                .email("user@email.com")
                .build();

        userDto2 = UserDto
                .builder()
                .name("name")
                .email("user2@email.com")
                .build();

        itemRequestDto = ItemRequestDto
                .builder()
                .description("item request description")
                .build();

        comment = CommentDto
                .builder()
                .text("first comment")
                .build();
    }

    @Test
    void createTest() {
        UserDto user = userController.addUser(userDto);
        ItemDto item = itemController.addItem(itemDto, 1);
        assertEquals(item.getId(), itemController.getItem(item.getId(), user.getId()).getId());
    }

    @Test
    void getOwnerItemsTest() {
        UserDto user = userController.addUser(userDto);
        ItemDto item = itemController.addItem(itemDto, 1);

        assertEquals(item.getId(), itemController.getOwnerItems(1).get(0).getId());
    }

    @Test
    void createWithRequestTest() {
        UserDto user = userController.addUser(userDto);
        ItemRequestDto itemRequest = itemRequestController.addRequest(itemRequestDto, user.getId());

        itemDto.setRequestId(1);
        itemDto.setComments(Collections.emptyList());

        UserDto user2 = userController.addUser(userDto2);
        ItemDto item = itemController.addItem(itemDto, 2);

        assertEquals(item, itemController.getItem(1, 2));
    }

    @Test
    void createByWrongUser() {
        assertThrows(NotFoundException.class, () -> itemController.addItem(itemDto, 1));
    }

    @Test
    void createWithWrongItemRequest() {
        itemDto.setRequestId(10);
        UserDto user = userController.addUser(userDto);

        assertThrows(NotFoundException.class, () -> itemController.addItem(itemDto, 10));
    }

    @Test
    void updateTest() {
        userController.addUser(userDto);
        itemController.addItem(itemDto, 1);
        itemController.editItem(1, itemDto2, 1);
        assertEquals(itemDto2.getDescription(), itemController.getItem(1, 1).getDescription());
    }

    @Test
    void updateForWrongItemTest() {
        assertThrows(NotFoundException.class, () -> itemController.editItem(1, itemDto, 1));
    }

    @Test
    void updateByWrongUserTest() {
        userController.addUser(userDto);
        itemController.addItem(itemDto, 1);
        assertThrows(NotFoundException.class, () -> itemController.editItem(1, itemDto2, 10));
    }

    @Test
    void searchTest() {
        userController.addUser(userDto);
        itemController.addItem(itemDto, 1);

        assertEquals(1, itemController.searchItems("Desc").size());
    }

    @Test
    void searchEmptyTest() {
        assertEquals(0, itemController.searchItems("").size());
    }

    @Test
    void createCommentTest() throws InterruptedException {
        UserDto user = userController.addUser(userDto);
        ItemDto item = itemController.addItem(itemDto, user.getId());
        UserDto user2 = userController.addUser(userDto2);

        bookingController.createBooking(BookingDto
                .builder()
                .start(LocalDateTime.now().plusSeconds(1))
                .end(LocalDateTime.now().plusSeconds(2))
                .itemId(item.getId()).build(), user2.getId());

        bookingController.acceptBooking(1, true, user.getId());
        TimeUnit.SECONDS.sleep(2);
        itemController.addComment(comment, item.getId(), user2.getId());
        assertEquals(1, itemController.getItem(1, 1).getComments().size());
    }

    @Test
    void createCommentByWrongUser() {
        assertThrows(NotFoundException.class, () -> itemController.addComment(comment, 1, 1));
    }

    @Test
    void createCommentToWrongItem() {
        UserDto user = userController.addUser(userDto);
        assertThrows(NotFoundException.class, () -> itemController.addComment(comment, 1, 1));
        ItemDto item = itemController.addItem(itemDto, 1);
        assertThrows(ValidationException.class, () -> itemController.addComment(comment, 1, 1));
    }
}
