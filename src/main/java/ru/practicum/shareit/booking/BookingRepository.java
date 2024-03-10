package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(int ownerId, BookingStatus status);

    List<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(int ownerId, LocalDateTime start, LocalDateTime end);

    List<Booking> findAllByItemOwnerIdAndStatusAndEndBeforeOrderByStartDesc(int ownerId, BookingStatus status, LocalDateTime end);

    List<Booking> findAllByItemOwnerIdAndStartAfterOrderByStartDesc(int ownerId, LocalDateTime start);

    List<Booking> findAllByItemOwnerIdOrderByStartDesc(int ownerId);

    List<Booking> findAllByBookerIdOrderByStartDesc(int bookerId);

    List<Booking> findAllByBookerIdAndItemIdAndStatusAndStartBefore(int bookerId, int itemId, BookingStatus status, LocalDateTime start);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(int ownerId, BookingStatus status);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(int ownerId, LocalDateTime start, LocalDateTime end);

    List<Booking> findAllByBookerIdAndStatusAndEndBeforeOrderByStartDesc(int ownerId, BookingStatus status, LocalDateTime end);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(int ownerId, LocalDateTime start);

    List<Booking> findAllByItemIdAndItemOwnerIdAndStatusOrderByStart(int itemId, int ownerId, BookingStatus status);

    List<Booking> findAllByItemOwnerIdAndStatusOrderByStart(int ownerId, BookingStatus status);
}
