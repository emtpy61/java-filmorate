package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User save(User user);

    Optional<User> findById(Long id);

    boolean existsById(Long id);

    List<User> findAll();

    List<User> findAllById(Iterable<Long> ids);

    void deleteById(Long id);

    void delete(User user);

    void deleteAll();
}
