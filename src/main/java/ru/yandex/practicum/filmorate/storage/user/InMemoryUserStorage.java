package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long currentId = 0;

    @Override
    public User save(User user) {
        if (!users.containsKey(user.getId())) {
            user.setId(++currentId);
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public boolean existsById(Long id) {
        return users.containsKey(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public List<User> findAllById(Iterable<Long> ids) {
        return StreamSupport.stream(ids.spliterator(), false)
                .map(this::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        users.remove(id);
    }

    @Override
    public void delete(User user) {
        this.deleteById(user.getId());
    }

    @Override
    public void deleteAll() {
        users.clear();
    }
}
