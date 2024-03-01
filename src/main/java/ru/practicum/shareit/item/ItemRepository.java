package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class ItemRepository {
    private final Map<Integer, Item> items = new HashMap<>();
    private int lastId = 1;

    public List<Item> getAllItems(int userId) {
        return items.values().stream().filter(item -> item.getOwner().getId() == userId).collect(Collectors.toList());
    }

    public Item getItem(int id) {
        return items.get(id);
    }

    public Item addItem(Item item) {
        item.setId(lastId);
        items.put(lastId, item);
        lastId++;
        return item;
    }

    public Item editItem(int id, Item editItem) {
        Item curItem = items.get(id);
        if (editItem.getName() != null) {
            curItem.setName(editItem.getName());
        }
        if (editItem.getDescription() != null) {
            curItem.setDescription(editItem.getDescription());
        }
        if (editItem.getAvailable() != null) {
            curItem.setAvailable(editItem.getAvailable());
        }
        return curItem;
    }

    public void delItem(int id) {
        items.remove(id);
    }

    public List<Item> search(String search) {
        String finalSearch = search.toLowerCase().trim();
        if (search.isBlank())
            return Collections.emptyList();
        return items.values().stream().filter(item -> equals(finalSearch, item)).collect(Collectors.toList());
    }

    private boolean equals(String search, Item item) {
        if (!item.getAvailable())
            return false;
        return hasOccurrence(search, item.getName()) || hasOccurrence(search, item.getDescription());
    }

    private boolean hasOccurrence(String search, String phrases) {
        List<String> names = Stream.of(phrases.split(" "))
                .map(s -> s.toLowerCase().trim()).collect(Collectors.toList());

        for (String name : names) {
            if (name.equals(search) || name.startsWith(search) || name.endsWith(search))
                return true;
        }
        return false;
    }
}
