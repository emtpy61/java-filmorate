package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dao.UserRepository;
import ru.yandex.practicum.filmorate.model.Users;

import javax.validation.Valid;
import java.util.List;

import static ru.yandex.practicum.filmorate.exception.NotFoundException.notFoundException;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<Users> getUsers() {
        List<Users> usersList = userRepository.findAll();
        log.info("Количество пользователей: {}", usersList.size());
        return usersList;
    }

    @PostMapping
    public Users createUser(@Valid @RequestBody Users user) {
        checkName(user);
        log.info("Создание пользователя: {}", user);
        return userRepository.save(user);
    }

    @PutMapping
    public Users updateUser(@Valid @RequestBody Users newUserData) {
        checkName(newUserData);
        long id = newUserData.getId();
        Users oldUserData = userRepository.findById(id)
                .orElseThrow(notFoundException("Не возможно обновить. Пользователь с id = {0} не найден.", id));
        log.info("Обновление пользователя: {}", oldUserData);
        log.info("Пользователь обновлен: {}", newUserData);
        return userRepository.save(newUserData);
    }

    private void checkName(Users user) {
        String userName = user.getName();
        if (userName == null || userName.isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
