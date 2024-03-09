package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepository {
    private final Map<Integer, User> users = new HashMap<>();
    private int lastId = 1;

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public User getUser(int id) {
        return users.get(id);
    }

    public boolean containsUser(int id) {
        return users.containsKey(id);
    }

    public List<User> getUserByEmail(String email) {
        return users.values().stream().filter(user -> user.getEmail().equalsIgnoreCase(email)).collect(Collectors.toList());
    }

    public User addUser(User user) {
        user.setId(lastId);
        users.put(lastId, user);
        lastId++;
        return user;
    }

    public User editUser(int id, User editUser) {
        User curUser = users.get(id);
        if (editUser.getEmail() != null) {
            curUser.setEmail(editUser.getEmail());
        }
        if (editUser.getName() != null) {
            curUser.setName(editUser.getName());
        }
        return curUser;
    }

    public void delUser(int id) {
        users.remove(id);
    }
}
