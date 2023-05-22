package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

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
        List<User> results = new ArrayList<User>();
        for (Long id : ids) {
            findById(id).ifPresent(results::add);
        }
        return results;
    }

    @Override
    public void deleteById(Long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователь с id = {0} не найден.", id);
        }
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
