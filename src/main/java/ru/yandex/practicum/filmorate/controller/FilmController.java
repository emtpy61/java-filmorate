package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dao.FilmRepository;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.List;

import static ru.yandex.practicum.filmorate.exception.NotFoundException.notFoundException;

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
    public Film updateFilm(@Valid @RequestBody Film newFilmData) {
        long id = newFilmData.getId();
        Film oldFilmData = filmRepository.findById(newFilmData.getId())
                .orElseThrow(notFoundException("Не возможно обновить. Фильм с id = {0} не найден.", id));
        log.info("Обновление фильма: {}", oldFilmData);
        log.info("Фильм обновлен: {}", newFilmData);
        return filmRepository.save(newFilmData);
    }
}
