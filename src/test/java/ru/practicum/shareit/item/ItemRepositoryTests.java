package ru.practicum.shareit.item;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.request.*;
import ru.practicum.shareit.user.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@DataJpaTest
public class ItemRepositoryTests {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private CommentRepository commentRepository;

    @AfterEach
    void tearDown() {
        commentRepository.deleteAll();
        itemRepository.deleteAll();
        itemRequestRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void searchTest() {
        User user = userRepository.save(User.builder().name("name").email("email@email.com").build());
        itemRepository.save(Item.builder().name("name").description("description").available(true).owner(user).build());
        List<Item> items = itemRepository.findAllByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCase("name", "desc");
        assertThat(items.size(), equalTo(1));
    }

    @Test
    void findAllByOwnerIdTest() {
        User user = userRepository.save(User.builder().name("name").email("email@email.com").build());
        itemRepository.save(Item.builder().name("name").description("description").available(true).owner(user).build());
        List<Item> items = itemRepository.findAllByOwnerId(user.getId());
        assertThat(items.size(), equalTo(1));
    }

    @Test
    void findAllByRequestIdTest() {
        User user = userRepository.save(User.builder().name("name").email("email@email.com").build());
        User user2 = userRepository.save(User.builder().name("name2").email("email2@email.com").build());
        ItemRequest itemRequest = itemRequestRepository.save(ItemRequest.builder().description("item request descr").userId(user2.getId()).created(LocalDateTime.now()).build());

        itemRepository.save(Item.builder().name("name").description("description").available(true).owner(user).requestId(itemRequest.getId()).build());
        assertThat(itemRepository.findAllByRequestId(itemRequest.getId()).size(), equalTo(1));
    }

    @Test
    void findAllCommentsByItemIdTest() {
        User user = userRepository.save(User.builder().name("name").email("email@email.com").build());
        User user2 = userRepository.save(User.builder().name("name2").email("email2@email.com").build());
        Item item = itemRepository.save(Item.builder().name("name").description("description").available(true).owner(user).build());

        commentRepository.save(Comment.builder()
                .text("text of comment")
                .item(item)
                .authorName(user2.getName())
                .created(LocalDateTime.now())
                .build());

        assertThat(commentRepository.findAllByItemId(item.getId()).size(), equalTo(1));
    }
}
