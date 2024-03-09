package ru.practicum.shareit.user;

import lombok.Data;

import javax.persistence.*;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @SequenceGenerator(name = "users_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    private int id;

    private String name;

    private String email;

//    @OneToMany(mappedBy = "owner")
//    @JsonManagedReference
//    private Set<Item> ownerItems;

//    @OneToMany(mappedBy = "tenant")
//    private Set<Item> tenantItems;

    public User() {
    }

    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
