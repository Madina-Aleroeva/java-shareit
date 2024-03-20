package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.ValidationException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class PageValidationTests {
    @Autowired
    private PageValidation pageValidation;

    @Test
    void testPageValidationZeros() {
        assertThrows(ValidationException.class, () -> pageValidation.checkPage(0, 0));
    }

    @Test
    void testPageValidationOneNull() {
        assertThrows(ValidationException.class, () -> pageValidation.checkPage(1, null));
    }

    @Test
    void testPageValidationNullOne() {
        assertThrows(ValidationException.class, () -> pageValidation.checkPage(null, 1));
    }

    @Test
    void testPageValidationValid() {
        assertDoesNotThrow(() -> pageValidation.checkPage(10, 10));
    }
}
