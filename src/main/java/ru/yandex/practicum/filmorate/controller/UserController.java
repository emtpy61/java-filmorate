package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dao.UserRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Users;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

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
    public Users updateUser(@Valid @RequestBody Users user) {
        checkName(user);

        Optional<Users> u = userRepository.findById(user.getId());
        if (u.isEmpty()) {
            log.error("Не возможно обновить. Пользователя с id = {} нет в базе.", user.getId());
            throw new NotFoundException(String.format("Не возможно обновить. Пользователя с id = %d нет в базе.",
                    user.getId()));
        }
        log.info("Обновление пользователя: {}", u.get());
        log.info("Пользователь обновлен: {}", user);
        return userRepository.save(user);
    }

    private void checkName(Users user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
    }
}
