package ru.practicum.shareit.user;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserControllerTests {
    @Autowired
    private UserController userController;

    private UserDto user;

    @BeforeEach
    void init() {
        user = UserDto.builder()
                .name("name")
                .email("user@email.com")
                .build();
    }

    @Test
    void createTest() {
        UserDto userDto = userController.addUser(user);
        assertEquals(userDto.getId(), userController.getUser(userDto.getId()).getId());
    }

    @Test
    void updateTest() {
        userController.addUser(user);
        UserDto userDto = UserDto.builder().name("update name").email("update@email.com").build();
        userController.editUser(1, userDto);
        assertEquals(userDto.getEmail(), userController.getUser(1).getEmail());
    }

    @Test
    void updateByWrongUserTest() {
        assertThrows(NotFoundException.class, () -> userController.editUser(1, user));
    }

    @Test
    void deleteTest() {
        UserDto userDto = userController.addUser(user);
        assertEquals(1, userController.getAllUsers().size());
        userController.delUser(userDto.getId());
        assertEquals(0, userController.getAllUsers().size());
    }

    @Test
    void getByWrongIdTest() {
        assertThrows(NotFoundException.class, () -> userController.getUser(1));
    }
}
