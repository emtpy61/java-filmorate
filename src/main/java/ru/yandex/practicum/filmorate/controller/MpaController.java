package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@AllArgsConstructor
@Slf4j
public class MpaController {
    private final MpaService mpaService;

    @GetMapping
    public List<Mpa> getUsers() {
        log.info("Получение списка всех МРА.");
        return mpaService.getMpa();
    }

    @GetMapping("/{id}")
    public Mpa findMpaById(@PathVariable int id) {
        log.info("Получение МРА по id {}.", id);
        return mpaService.findMpaById(id);
    }
}
