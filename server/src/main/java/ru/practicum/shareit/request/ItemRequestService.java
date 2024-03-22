package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRequestMapper itemRequestMapper;
    private final UserService userService;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    public ItemRequest findItemRequest(Long requestId) {
        return itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("not found item request with id %d", requestId)));
    }

    public ItemRequestDto addRequest(ItemRequestDto itemRequestDto, Long sharerId) {
        ItemRequest itemRequest = itemRequestMapper.convertToModel(itemRequestDto);
        userService.findUser(sharerId); // check sharerId
        itemRequest.setUserId(sharerId);
        itemRequest.setCreated(LocalDateTime.now());

        itemRequestRepository.save(itemRequest);
        itemRequestRepository.flush();
        return itemRequestMapper.convertToDto(itemRequest);
    }

    public List<ItemRequestDto> getUserRequests(Long sharerId, Long from, Long size) {
        userService.findUser(sharerId); // check sharerId

        Map<Long, List<ItemDto>> itemsByRequestId = itemRepository.findAll()
                .stream()
                .map(itemMapper::convertToDto)
                .collect(Collectors.groupingBy(ItemDto::getRequestId));

        return itemRequestRepository.findAllByUserIdOrderByCreatedDesc(sharerId)
                .stream().map(itemRequestMapper::convertToDto)
                .peek(request -> request.setItems(itemsByRequestId.getOrDefault(request.getId(), Collections.emptyList())))
                .collect(Collectors.toList());
    }

    public List<ItemRequestDto> getAllRequests(Long sharerId, Long from, Long size) {
        userService.findUser(sharerId); // check sharerId

        Map<Long, List<ItemDto>> itemsByRequestId = itemRepository.findAll()
                .stream()
                .map(itemMapper::convertToDto)
                .collect(Collectors.groupingBy(ItemDto::getRequestId));

        return itemRequestRepository.findAllByUserIdNotOrderByCreatedDesc(sharerId)
                .stream().map(itemRequestMapper::convertToDto)
                .peek(request -> request.setItems(itemsByRequestId.getOrDefault(request.getId(), Collections.emptyList())))
                .collect(Collectors.toList());
    }


    public ItemRequestDto getRequest(Long requestId, Long sharerId) {
        userService.findUser(sharerId); // check sharerId
        ItemRequest itemRequest = findItemRequest(requestId);
        itemRequest.setItems(itemRepository.findAllByRequestId(itemRequest.getId()));
        return itemRequestMapper.convertToDto(itemRequest);
    }
}
