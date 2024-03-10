package ru.practicum.shareit.item;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CommentDto {
    private Integer id;

    private String text;

    private String authorName;

    private LocalDateTime created;

    public CommentDto(Integer id, String text, String authorName, LocalDateTime created) {
        this.id = id;
        this.text = text;
        this.authorName = authorName;
        this.created = created;
    }
}
