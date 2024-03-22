package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwnerId(Long id);

    List<Item> findAllByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCase(String name, String description);

    List<Item> findAllByRequestId(Long requestId);
}
