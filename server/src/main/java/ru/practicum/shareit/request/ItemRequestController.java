package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private static final String SHARER_USER_ID = "X-Sharer-User-Id";
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto addRequest(@RequestBody ItemRequestDto itemRequestDto,
                                     @RequestHeader(value = SHARER_USER_ID) Long sharerId) {
        return itemRequestService.addRequest(itemRequestDto, sharerId);
    }

    @GetMapping
    public List<ItemRequestDto> getUserRequests(@RequestHeader(value = SHARER_USER_ID) Long sharerId,
                                                @RequestParam(required = false) Long from,
                                                @RequestParam(required = false) Long size) {
        return itemRequestService.getUserRequests(sharerId, from, size);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequests(@RequestParam(required = false) Long from,
                                               @RequestParam(required = false) Long size,
                                               @RequestHeader(value = SHARER_USER_ID) Long sharerId) {
        return itemRequestService.getAllRequests(sharerId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequest(@PathVariable Long requestId,
                                     @RequestHeader(value = SHARER_USER_ID) Long sharerId) {
        return itemRequestService.getRequest(requestId, sharerId);
    }
}
