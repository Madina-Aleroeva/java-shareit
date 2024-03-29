package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@AutoConfigureJsonTesters
public class ItemDtoJsonTests {
    @Autowired
    JacksonTester<ItemDto> json;

    @Test
    void testItemDto() throws Exception {
        ItemDto itemDto = ItemDto
                .builder()
                .id(1)
                .name("item")
                .available(true)
                .description("descriptionOfItem")
                .build();

        JsonContent<ItemDto> result = json.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("item");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("descriptionOfItem");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isTrue();
    }
}
