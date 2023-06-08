package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ru.yandex.practicum.filmorate.exception.NotFoundException.notFoundException;

@Service
@AllArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User addUser(User user) {
        checkName(user);
        return userStorage.create(user);
    }

    public User findUserById(int id) {
        return userStorage.findById(id)
                .orElseThrow(notFoundException("Пользователь с id = {0} не найден.", id));
    }

    public List<User> getUsers() {
        return userStorage.findAll();
    }

    public User updateUser(User user) {
        checkUserExists(user.getId());
        checkName(user);
        return userStorage.update(user);
    }

    public void deleteUserById(int id) {
        checkUserExists(id);
        userStorage.deleteById(id);
    }

    public void clearUsers() {
        userStorage.deleteAll();
    }

    public void addFriend(int id, int friendId) {
        checkUserExists(id);
        checkUserExists(friendId);
        userStorage.addFriend(id, friendId);
    }

    public List<User> getFriends(int id) {
        checkUserExists(id);
        return userStorage.findAllById(userStorage.getFriendsIds(id));
    }

    public List<User> getCommonFriends(int id, int otherId) {
        checkUserExists(id);
        checkUserExists(otherId);
        Set<Integer> intersection = new HashSet<>(userStorage.getFriendsIds(id));
        intersection.retainAll(userStorage.getFriendsIds(otherId));
        return userStorage.findAllById(intersection);
    }

    public void deleteFriend(int id, int friendId) {
        checkUserExists(id);
        checkUserExists(friendId);
        userStorage.deleteFriend(id, friendId);
    }

    private void checkUserExists(int id) {
        if (userStorage.notExistsById(id)) {
            throw new NotFoundException("Пользователь с id = {0} не найден.", id);
        }
    }

    private void checkName(User user) {
        String userName = user.getName();
        if (userName == null || userName.isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
