package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingInfoRepository extends JpaRepository<BookingInfo, Integer> {
}
