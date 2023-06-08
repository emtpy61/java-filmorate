package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@AllArgsConstructor
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public List<Film> getFilms() {
        log.info("Получение списка всех фильмов");
        return filmService.getFilms();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Создание фильма: {}", film);
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Обновление фильма: {}", film);
        return filmService.updateFilm(film);
    }

    @DeleteMapping
    public void clearFilms() {
        log.info("Очистка списка фильмов.");
        filmService.clearFilms();
    }

    @DeleteMapping("/{id}")
    public void deleteFilmById(@PathVariable int id) {
        log.info("Удаление фильма по id {}.", id);
        filmService.deleteFilmById(id);
    }

    @GetMapping("/{id}")
    public Film findFilmById(@PathVariable int id) {
        log.info("Получение фильма по id {}.", id);
        return filmService.findFilmById(id);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLikeFilm(@PathVariable int filmId, @PathVariable int userId) {
        log.debug("Пользователь с id {} ставит лайк фильму с id {}", userId, filmId);
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLikeFilm(@PathVariable int filmId, @PathVariable int userId) {
        log.debug("Пользователь с id {} удаляет лайк к фильму с id {}", userId, filmId);
        filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(
            @RequestParam(value = "count", required = false, defaultValue = "10") int count) {
        return filmService.popularFilms(count);
    }
}
