package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.exception.ValidationException;

import java.util.HashMap;
import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";
    private final BookingValidator bookingValidator;

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl,
                         RestTemplateBuilder builder,
                         BookingValidator bookingValidator) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build()
        );
        this.bookingValidator = bookingValidator;
    }

    public ResponseEntity<Object> getBookerBookings(Long userId, String state, Long from, Long size) {
        bookingValidator.checkStatus(state);
        bookingValidator.checkPage(from, size);
        Map<String, Object> parameters = getBookParams(state, from, size);
        String path = getBookPath(from, size);
        return get(path, userId, parameters);
    }

    public ResponseEntity<Object> getOwnerBookings(Long userId, String state, Long from, Long size) {
        bookingValidator.checkStatus(state);
        bookingValidator.checkPage(from, size);
        Map<String, Object> parameters = getBookParams(state, from, size);
        String path = "/owner" + getBookPath(from, size);
        return get(path, userId, parameters);
    }

    private String getBookPath(Long from, Long size) {
        return "?state={state}&from={from}&size={size}";
        /*if (from == null && size == null) {
            return "?state={state}";
        } else {

        }*/
    }

    private Map<String, Object> getBookParams(String state, Long from, Long size) {
        Map<String, Object> map = new HashMap<>();
        map.put("state", state);
        map.put("from", from);
        map.put("size", size);
        return map;
        /*if (from == null && size == null) {
            return Map.of("state", state);
        } else {

        }*/
    }

    public ResponseEntity<Object> acceptBooking(Long userId, Long bookingId, Boolean approved) {
        if (approved == null) {
            throw new ValidationException("approved should be set");
        }
        String path = "/" + bookingId.toString() + "?approved={approved}";
        return patch(path, userId, Map.of("approved", approved), null);
    }

    public ResponseEntity<Object> bookItem(Long userId, BookingDto requestDto) {
        bookingValidator.checkDates(requestDto);
        return post("", userId, requestDto);
    }

    public ResponseEntity<Object> getBooking(Long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }


    private static Map<String, Object> getBookingsParams(Long from, Long size, BookingStatusDTO statusDto) {
        return Map.of(
                "state", statusDto.name(),
                "from", from == null ? "null" : from,
                "size", size == null ? "null" : size
        );
    }
}
