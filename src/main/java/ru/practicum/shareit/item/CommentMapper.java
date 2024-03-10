package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;

@Service
public class CommentMapper {
    public Comment convertToModel(CommentDto obj) {
        return new Comment(obj.getId(), obj.getText(), obj.getAuthorName(), obj.getCreated());
    }

    public CommentDto convertToDto(Comment obj) {
        return new CommentDto(obj.getId(), obj.getText(), obj.getAuthorName(), obj.getCreated());
    }

}
