package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ru.yandex.practicum.filmorate.exception.NotFoundException.notFoundException;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public List<User> getUsers() {
        return userStorage.findAll();
    }

    public User findUserById(Long id) {
        return userStorage.findById(id)
                .orElseThrow(notFoundException("Пользователь с id = {0} не найден.", id));
    }

    public User addUser(User user) {
        checkName(user);
        return userStorage.save(user);
    }

    public User updateUser(User user) {
        if (!userStorage.existsById(user.getId())) {
            throw new NotFoundException("Пользователь с id = {0} не найден.", user.getId());
        }
        checkName(user);
        return userStorage.save(user);
    }

    public void clearUsers() {
        userStorage.deleteAll();
    }

    public void deleteUserById(Long id) {
        if (!userStorage.existsById(id)) {
            throw new NotFoundException("Пользователь с id = {0} не найден.", id);
        }
        userStorage.deleteById(id);
    }

    public void addFriend(Long id, Long friendId) {
        User user = userStorage.findById(id)
                .orElseThrow(notFoundException("Пользователь с id = {0} не найден.", id));
        User friend = userStorage.findById(friendId)
                .orElseThrow(notFoundException("Невозможно добавить в друзья.Пользователь с id = {0} не найден.",
                        friendId));
        userStorage.save(user.toBuilder()
                .friend(friendId)
                .build());
        userStorage.save(friend.toBuilder()
                .friend(id)
                .build());
    }

    public void deleteFriend(Long id, Long friendId) {
        User user = userStorage.findById(id)
                .orElseThrow(notFoundException("Пользователь с id = {0} не найден.", id));
        User friend = userStorage.findById(id)
                .orElseThrow(notFoundException("Невозможно удалить из друзей.Пользователя с id = {0} нет в друзьях.",
                        friendId));
        Set<Long> friends = new HashSet<>(user.getFriends());
        Set<Long> otherFriends = new HashSet<>(friend.getFriends());
        friends.remove(friendId);
        otherFriends.remove(id);
        userStorage.save(user.toBuilder()
                .friends(friends)
                .build());
        userStorage.save(friend.toBuilder()
                .friends(otherFriends)
                .build());
    }

    public List<User> getFriends(Long id) {
        User user = userStorage.findById(id)
                .orElseThrow(notFoundException("Пользователь с id = {0} не найден.", id));
        return userStorage.findAllById(user.getFriends());
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        User user = userStorage.findById(id)
                .orElseThrow(notFoundException("Пользователь с id = {0} не найден.", id));
        User otherUser = userStorage.findById(id)
                .orElseThrow(notFoundException("Пользователь с id = {0} не найден.", id));
        Set<Long> intersection = new HashSet<>(user.getFriends());
        intersection.retainAll(otherUser.getFriends());
        intersection.remove(id);
        intersection.remove(otherId);
        return userStorage.findAllById(intersection);
    }

    private void checkName(User user) {
        String userName = user.getName();
        if (userName == null || userName.isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
