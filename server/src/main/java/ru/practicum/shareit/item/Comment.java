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
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    private String text;

    private String authorName;

    private LocalDateTime created;
}
