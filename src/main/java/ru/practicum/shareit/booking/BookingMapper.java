package ru.practicum.shareit.booking;

public class BookingMapper {
    public static Booking convertToModel(BookingDto dto) {
        return new Booking(dto.getId(), dto.getItem(), dto.getBooker(), dto.getStart(),
                dto.getEnd(), dto.getFeedback(), dto.getStatus());
    }

    public static BookingDto convertToDto(Booking model) {
        return new BookingDto(model.getId(), model.getItem(), model.getBooker(), model.getStart(),
                model.getEnd(), model.getFeedback(), model.getStatus());
    }
}
