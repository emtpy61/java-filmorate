package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dao.FilmRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    @Autowired
    private FilmRepository filmRepository;

    @GetMapping
    public List<Film> getFilms() {
        List<Film> filmsList = filmRepository.findAll();
        log.info("Количество фильмов: {}", filmsList.size());
        return filmsList;
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Создание фильма: {}", film);
        return filmRepository.save(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        Optional<Film> f = filmRepository.findById(film.getId());
        if (f.isEmpty()) {
            throw new NotFoundException(String.format("Не возможно обновить.Фильма с id = %d нет в базе.",
                    film.getId()));
        }
        log.info("Обновление фильма: {}", f.get());
        log.info("Фильм обновлен: {}", film);
        return filmRepository.save(film);
    }
}
