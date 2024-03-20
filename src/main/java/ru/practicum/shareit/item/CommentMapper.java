package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;

@Service
public class CommentMapper {
    public Comment convertToModel(CommentDto obj) {
        return Comment.builder()
                .id(obj.getId())
                .text(obj.getText())
                .authorName(obj.getAuthorName())
                .created(obj.getCreated())
                .build();
    }

    public CommentDto convertToDto(Comment obj) {
        return CommentDto.builder()
                .id(obj.getId())
                .text(obj.getText())
                .authorName(obj.getAuthorName())
                .created(obj.getCreated())
                .build();
    }
}
