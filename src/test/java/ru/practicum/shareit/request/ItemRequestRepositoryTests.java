package ru.practicum.shareit.request;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@DataJpaTest
public class ItemRequestRepositoryTests {
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        itemRequestRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findAllByUserIdOrderByCreatedDescTest() {
        User user = userRepository.save(User.builder().name("name").email("email@email.com").build());
        itemRequestRepository.save(ItemRequest.builder().description("description").userId(user.getId())
                .created(LocalDateTime.now()).build());

        List<ItemRequest> items = itemRequestRepository.findAllByUserIdOrderByCreatedDesc(user.getId());
        assertThat(items.size(), equalTo(1));
    }

    @Test
    void findAllByUserIdNotOrderByCreatedDescTest() {
        User user = userRepository.save(User.builder().name("name").email("email@email.com").build());
        itemRequestRepository.save(ItemRequest.builder().description("description").userId(user.getId())
                .created(LocalDateTime.now()).build());

        assertThat(itemRequestRepository.findAllByUserIdNotOrderByCreatedDesc(user.getId())
                .size(), equalTo(0));

        User user2 = userRepository.save(User.builder().name("name2").email("email2@email.com").build());

        assertThat(itemRequestRepository.findAllByUserIdNotOrderByCreatedDesc(user2.getId())
                .size(), equalTo(1));
    }
}
