package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;

@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";
    private final UserValidator userValidator;

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl,
                      RestTemplateBuilder builder,
                      UserValidator userValidator) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build()
        );
        this.userValidator = userValidator;
    }

    public ResponseEntity<Object> getAllUsers() {
        return get("");
    }

    public ResponseEntity<Object> getUser(Long userId) {
        userValidator.checkUserId(userId);
        String path = "/" + userId.toString();
        return get(path);
    }

    public ResponseEntity<Object> addUser(UserDto userDto) {
        userValidator.checkUserEmail(userDto);
        return post("", userDto);
    }

    public ResponseEntity<Object> editUser(Long userId, UserDto userDto) {
        userValidator.checkUserId(userId);
        String path = "/" + userId.toString();
        return patch(path, userDto);
    }

    public ResponseEntity<Object> delUser(Long userId) {
        userValidator.checkUserId(userId);
        String path = "/" + userId.toString();
        return delete(path);
    }

}
