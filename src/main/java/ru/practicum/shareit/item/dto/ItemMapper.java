package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.CommentDto;
import ru.practicum.shareit.item.CommentMapper;
import ru.practicum.shareit.item.Item;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemMapper {
    private final CommentMapper commentMapper;

    public Item convertToModel(ItemDto obj) {
        List<CommentDto> commentDtos = obj.getComments();
        List<Comment> comments = commentDtos == null ? null : commentDtos
                .stream().map(commentMapper::convertToModel).collect(Collectors.toList());
        return new Item(obj.getId(), obj.getName(), obj.getDescription(),
                obj.getAvailable(), comments);
    }

    public ItemDto convertToDto(Item obj) {
        List<Comment> comments = obj.getComments();
        List<CommentDto> commentDtos = comments == null ? null : comments
                .stream().map(commentMapper::convertToDto).collect(Collectors.toList());
        return new ItemDto(obj.getId(), obj.getName(), obj.getDescription(),
                obj.getAvailable(), commentDtos);
    }

}


