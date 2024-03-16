package ru.practicum.shareit.item;

import lombok.*;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "items")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@AllArgsConstructor
public class Item {
    @Id
    @SequenceGenerator(name = "items_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "items_seq")
    @EqualsAndHashCode.Include
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    private User owner;

    private String name;

    private String description;

    @OneToMany(mappedBy = "item", fetch = FetchType.EAGER)
    private List<Comment> comments;

    private Boolean available;

    private Integer requestId;
}
