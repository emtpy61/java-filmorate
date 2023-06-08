package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User create(User user);

    boolean notExistsById(int id);

    Optional<User> findById(int id);

    List<User> findAll();

    List<User> findAllById(Iterable<Integer> ids);

    User update(User user);

    void deleteById(int id);

    void deleteAll();

    void addFriend(int id, int friendId);

    List<Integer> getFriendsIds(int id);

    void deleteFriend(int id, int friendId);
}
