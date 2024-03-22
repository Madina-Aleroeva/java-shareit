package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private static final String SHARER_USER_ID = "X-Sharer-User-Id";
    private final ItemRequestClient itemRequestClient;

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequest(@PathVariable Long requestId,
                                             @RequestHeader(value = SHARER_USER_ID, required = false) Long userId) {
        return itemRequestClient.getRequest(userId, requestId);
    }

    @PostMapping
    public ResponseEntity<Object> addRequest(@RequestBody ItemRequestDto itemRequestDto,
                                             @RequestHeader(value = SHARER_USER_ID, required = false) Long userId) {
        return itemRequestClient.addRequest(userId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getUserRequests(@RequestHeader(value = SHARER_USER_ID, required = false) Long userId,
                                                  @RequestParam(required = false) Long from,
                                                  @RequestParam(required = false) Long size) {
        return itemRequestClient.getUserRequests(userId, from, size);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestHeader(value = SHARER_USER_ID, required = false) Long userId,
                                                 @RequestParam(required = false) Long from,
                                                 @RequestParam(required = false) Long size) {
        return itemRequestClient.getAllRequests(userId, from, size);
    }


}
