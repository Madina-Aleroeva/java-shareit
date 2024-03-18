package ru.practicum.shareit.request;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemRequestControllerTests {
    @Autowired
    private ItemRequestController itemRequestController;

    @Autowired
    private UserController userController;

    private ItemRequestDto itemRequestDto;

    private UserDto userDto;

    private UserDto userDto2;

    @BeforeEach
    void init() {
        itemRequestDto = ItemRequestDto
                .builder()
                .description("item request description")
                .build();

        userDto = UserDto
                .builder()
                .name("name")
                .email("user@email.com")
                .build();

        userDto2 = UserDto
                .builder()
                .name("name2")
                .email("user2@email.com")
                .build();
    }

    @Test
    void createTest() {
        UserDto user = userController.addUser(userDto);
        ItemRequestDto itemRequest = itemRequestController.addRequest(itemRequestDto, user.getId());
        assertEquals(1, itemRequestController.getRequest(itemRequest.getId(), user.getId()).getId());
    }

    @Test
    void createByWrongUserTest() {
        assertThrows(NotFoundException.class, () -> itemRequestController.addRequest(itemRequestDto, 1));
    }

    @Test
    void getAllByUserTest() {
        UserDto user = userController.addUser(userDto);
        ItemRequestDto itemRequest = itemRequestController.addRequest(itemRequestDto, user.getId());
        assertEquals(1, itemRequestController.getUserRequests(user.getId(), 0, 10).size());
    }

    @Test
    void getAllByUserWithWrongUserTest() {
        assertThrows(NotFoundException.class, () -> itemRequestController.getUserRequests(1, 0, 10));
    }

    @Test
    void getAll() {
        UserDto user = userController.addUser(userDto);
        ItemRequestDto itemRequest = itemRequestController.addRequest(itemRequestDto, user.getId());
        assertEquals(0, itemRequestController.getAllRequests(0, 10, user.getId()).size());
        UserDto user2 = userController.addUser(userDto2);
        assertEquals(1, itemRequestController.getAllRequests(0, 10, user2.getId()).size());
    }

    @Test
    void getAllByWrongUser() {
        assertThrows(NotFoundException.class, () -> itemRequestController.getAllRequests(0, 10, 1));
    }
}
