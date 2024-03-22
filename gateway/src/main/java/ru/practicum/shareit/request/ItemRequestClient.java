package ru.practicum.shareit.request;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.BookingValidator;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserValidator;

import java.util.HashMap;
import java.util.Map;

@Service
public class ItemRequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";
    private final ItemRequestValidator itemRequestValidator;
    private final UserValidator userValidator;
    private final BookingValidator bookingValidator;

    @Autowired
    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl,
                             RestTemplateBuilder builder,
                             ItemRequestValidator itemRequestValidator,
                             UserValidator userValidator,
                             BookingValidator bookingValidator) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build()
        );
        this.itemRequestValidator = itemRequestValidator;
        this.userValidator = userValidator;
        this.bookingValidator = bookingValidator;
    }

    public ResponseEntity<Object> getRequest(Long userId, Long requestId) {
        userValidator.checkUserId(userId);
        itemRequestValidator.checkRequestId(requestId);
        String path = "/" + requestId.toString();
        return get(path, userId);
    }

    public ResponseEntity<Object> addRequest(Long userId, ItemRequestDto itemRequestDto) {
        userValidator.checkUserId(userId);
        itemRequestValidator.checkRequest(itemRequestDto);
        return post("", userId, itemRequestDto);
    }

    public ResponseEntity<Object> getUserRequests(Long userId, Long from, Long size) {
        userValidator.checkUserId(userId);
        bookingValidator.checkPage(from, size);
        String path = "?from={from}&size={size}";
        Map<String, Object> map = new HashMap<>();
        map.put("from", from);
        map.put("size", size);
        return get(path, userId, map);
    }

    public ResponseEntity<Object> getAllRequests(Long userId, Long from, Long size) {
        userValidator.checkUserId(userId);
        bookingValidator.checkPage(from, size);
        String path = "/all?from={from}&size={size}";
        Map<String, Object> map = new HashMap<>();
        map.put("from", from);
        map.put("size", size);
        return get(path, userId, map);
    }
}
