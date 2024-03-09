package ru.practicum.shareit.booking;

import ru.practicum.shareit.item.Item;

import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */
public class Booking {
    private Item item;
    private LocalDate firstDate;
    private LocalDate lastDate;
    private boolean confirmed;
    private String feedback;
}
