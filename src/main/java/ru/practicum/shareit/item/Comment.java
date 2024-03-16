package ru.practicum.shareit.item;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "comments")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Comment {
    @Id
    @SequenceGenerator(name = "comments_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comments_seq")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Item item;

    private String text;

    private String authorName;

    private LocalDateTime created;
}
