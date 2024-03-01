package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private static final String SHARER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ItemDto addItem(@RequestBody ItemDto itemDto,
                           @RequestHeader(value = SHARER_USER_ID, required = false) Integer sharerId) {
        return itemService.addItem(itemDto, sharerId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto editItem(@PathVariable int itemId, @RequestBody ItemDto itemDto,
                            @RequestHeader(value = SHARER_USER_ID, required = false) Integer sharerId) {
        return itemService.editItem(itemId, itemDto, sharerId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable int itemId) {
        return itemService.getItem(itemId);
    }

    @GetMapping
    public List<ItemDto> getOwnerItems(@RequestHeader(value = SHARER_USER_ID, required = false) Integer sharerId) {
        return itemService.getOwnerItems(sharerId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        return itemService.search(text);
    }


}
