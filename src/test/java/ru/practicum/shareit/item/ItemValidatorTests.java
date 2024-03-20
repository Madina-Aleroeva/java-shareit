package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.ValidationException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class ItemValidatorTests {
    @Autowired
    private ItemValidator itemValidator;
    private Item item;

    @BeforeEach
    void init() {
        item = Item.builder().build();
    }

    @Test
    void checkItemFieldsNameIsNullTest() {
        item.setName(null);
        item.setDescription("Some description");
        item.setAvailable(true);

        assertThrows(ValidationException.class, () -> itemValidator.checkItemFields(item));
    }

    @Test
    void checkItemFieldsNameIsBlankTest() {
        item.setName("");
        item.setDescription("Some description");
        item.setAvailable(true);

        assertThrows(ValidationException.class, () -> itemValidator.checkItemFields(item));
    }

    @Test
    void checkItemFieldsDescriptionIsNullTest() {
        item.setName("Some name");
        item.setDescription(null);
        item.setAvailable(true);

        assertThrows(ValidationException.class, () -> itemValidator.checkItemFields(item));
    }

    @Test
    void checkItemFieldsDescriptionIsBlankTest() {
        item.setName("Some name");
        item.setDescription("");
        item.setAvailable(true);

        assertThrows(ValidationException.class, () -> itemValidator.checkItemFields(item));
    }

    @Test
    void checkItemFieldsAvailableIsNullTest() {
        item.setName("Some name");
        item.setDescription("Some description");
        item.setAvailable(null);

        assertThrows(ValidationException.class, () -> itemValidator.checkItemFields(item));
    }

    @Test
    void checkItemFieldsAllValidTest() {
        item.setName("Some name");
        item.setDescription("Some description");
        item.setAvailable(true);

        assertDoesNotThrow(() -> itemValidator.checkItemFields(item));
    }
}
