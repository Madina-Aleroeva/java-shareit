package ru.practicum.shareit.item;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserValidator;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";
    private final ItemValidator itemValidator;
    private final UserValidator userValidator;


    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl,
                      RestTemplateBuilder builder,
                      ItemValidator itemValidator,
                      UserValidator userValidator) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build()
        );
        this.itemValidator = itemValidator;
        this.userValidator = userValidator;
    }

    public ResponseEntity<Object> addItem(Long userId, ItemDto itemDto) {
        userValidator.checkUserId(userId);
        itemValidator.checkAddItem(itemDto);
        return post("", userId, itemDto);
    }

    public ResponseEntity<Object> addComment(Long userId, Long itemId, CommentDto commentDto) {
        userValidator.checkUserId(userId);
        itemValidator.checkItemId(itemId);
        itemValidator.checkComment(commentDto);
        String path = "/" + itemId + "/comment";
        return post(path, userId, commentDto);
    }

    public ResponseEntity<Object> editItem(Long userId, Long itemId, ItemDto itemDto) {
        userValidator.checkUserId(userId);
        itemValidator.checkItemId(itemId);
        itemValidator.checkEditItem(itemDto);
        String path = "/" + itemId;
        return patch(path, userId, itemDto);
    }

    public ResponseEntity<Object> getItem(Long userId, Long itemId) {
        userValidator.checkUserId(userId);
        itemValidator.checkItemId(itemId);
        String path = "/" + itemId;
        return get(path, userId);
    }

    public ResponseEntity<Object> getOwnerItems(Long userId) {
        userValidator.checkUserId(userId);
        return get("", userId);
    }

    public ResponseEntity<Object> search(String text) {
        String path = "/search?text={text}";
        return get(path, null, Map.of("text", text));
    }
}
