package ru.practicum.shareit.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
public class Item {
    @Id
    @SequenceGenerator(name = "items_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "items_seq")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;

    private String name;

    private String description;

    @OneToMany(mappedBy = "item")
    private List<Comment> comments;

    private Boolean available;

    public Item(Integer id, String name, String description, Boolean available, List<Comment> comments) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.comments = comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;

        Item item = (Item) o;

        return getId() != null ? getId().equals(item.getId()) : item.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
