package ru.practicum.shareit.item;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @SequenceGenerator(name = "comments_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comments_seq")
    private int id;

    @ManyToOne
    @JsonBackReference
    private Item item;

    private String text;

    private String authorName;

    private LocalDateTime created;

}
