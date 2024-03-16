package ru.practicum.shareit.request;

import lombok.*;
import ru.practicum.shareit.item.Item;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "item_requests")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class ItemRequest {
    @Id
    @SequenceGenerator(name = "item_requests_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_requests_seq")
    @EqualsAndHashCode.Include
    private Integer id;

    private Integer itemId;

    private Integer userId;

    private String name;

    private String description;

    private LocalDateTime created;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Item> items;
}
