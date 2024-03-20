package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.PageValidation;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
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
    private final PageValidation pageValidation;

    public ItemRequest findItemRequest(Integer requestId) {
        if (requestId == null) {
            throw new ValidationException("item request id can't be null");
        }
        return itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("not found item request with id %d", requestId)));
    }

    public ItemRequestDto addRequest(ItemRequestDto itemRequestDto, Integer sharerId) {
        ItemRequest itemRequest = itemRequestMapper.convertToModel(itemRequestDto);
        userService.findUser(sharerId); // check sharerId
        itemRequest.setUserId(sharerId);
        if (itemRequestDto.getDescription() == null || itemRequestDto.getDescription().isEmpty()) {
            throw new ValidationException("description can't be empty");
        }
        itemRequest.setCreated(LocalDateTime.now());

        itemRequestRepository.save(itemRequest);
        itemRequestRepository.flush();
        return itemRequestMapper.convertToDto(itemRequest);
    }

    public List<ItemRequestDto> getUserRequests(Integer sharerId, Integer from, Integer size) {
        userService.findUser(sharerId); // check sharerId
        pageValidation.checkPage(from, size);

        Map<Integer, List<ItemDto>> itemsByRequestId = itemRepository.findAll()
                .stream()
                .map(itemMapper::convertToDto)
                .collect(Collectors.groupingBy(ItemDto::getRequestId));

        return itemRequestRepository.findAllByUserIdOrderByCreatedDesc(sharerId)
                .stream().map(itemRequestMapper::convertToDto)
                .peek(request -> request.setItems(itemsByRequestId.getOrDefault(request.getId(), Collections.emptyList())))
                .collect(Collectors.toList());
    }

    public List<ItemRequestDto> getAllRequests(Integer sharerId, Integer from, Integer size) {
        userService.findUser(sharerId); // check sharerId
        pageValidation.checkPage(from, size);

        Map<Integer, List<ItemDto>> itemsByRequestId = itemRepository.findAll()
                .stream()
                .map(itemMapper::convertToDto)
                .collect(Collectors.groupingBy(ItemDto::getRequestId));

        return itemRequestRepository.findAllByUserIdNotOrderByCreatedDesc(sharerId)
                .stream().map(itemRequestMapper::convertToDto)
                .peek(request -> request.setItems(itemsByRequestId.getOrDefault(request.getId(), Collections.emptyList())))
                .collect(Collectors.toList());
    }


    public ItemRequestDto getRequest(Integer requestId, Integer sharerId) {
        userService.findUser(sharerId); // check sharerId
        ItemRequest itemRequest = findItemRequest(requestId);
        itemRequest.setItems(itemRepository.findAllByRequestId(itemRequest.getId()));
        return itemRequestMapper.convertToDto(itemRequest);
    }
}
