package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private static final String SHARER_USER_ID = "X-Sharer-User-Id";
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestBody ItemDto itemDto,
                                          @RequestHeader(value = SHARER_USER_ID, required = false) Long sharerId) {
        return itemClient.addItem(sharerId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestBody CommentDto commentDto,
                                             @PathVariable Long itemId,
                                             @RequestHeader(value = SHARER_USER_ID, required = false) Long sharerId) {
        return itemClient.addComment(sharerId, itemId, commentDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> editItem(@PathVariable Long itemId, @RequestBody ItemDto itemDto,
                                           @RequestHeader(value = SHARER_USER_ID, required = false) Long sharerId) {
        return itemClient.editItem(sharerId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@PathVariable Long itemId,
                                          @RequestHeader(value = SHARER_USER_ID, required = false) Long sharerId) {
        return itemClient.getItem(sharerId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getOwnerItems(@RequestHeader(value = SHARER_USER_ID, required = false) Long sharerId) {
        return itemClient.getOwnerItems(sharerId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam String text) {
        return itemClient.search(text);
    }


}
