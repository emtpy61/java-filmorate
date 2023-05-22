package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.exception.NotFoundException.notFoundException;

@Slf4j
@Service
@AllArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;


    public List<Film> getFilms() {
        return filmStorage.findAll();
    }

    public Film findFilmById(Long id) {
        return filmStorage.findById(id)
                .orElseThrow(notFoundException("Фильм с id = {0} не найден.", id));
    }

    public Film addFilm(Film film) {
        return filmStorage.save(film);
    }

    public Film updateFilm(Film film) {
        if (!filmStorage.existsById(film.getId())) {
            throw new NotFoundException("Фильм с id = {0} не найден.", film.getId());
        }
        return filmStorage.save(film);
    }

    public void clearFilms() {
        filmStorage.deleteAll();
    }

    public void deleteFilmById(Long id) {
        filmStorage.deleteById(id);
    }

    public void addLike(Long filmId, Long userId) {
        Film film = filmStorage.findById(filmId)
                .orElseThrow(notFoundException("Фильм с id = {0} не найден.", filmId));
        filmStorage.save(film.toBuilder()
                .like(userId)
                .build());
    }

    public void deleteLike(Long filmId, Long userId) {
        Film film = filmStorage.findById(filmId)
                .orElseThrow(notFoundException("Фильм с id = {0} не найден.", filmId));
        Set<Long> likes = new HashSet<>(film.getLikes());
        if (!likes.remove(userId)) {
            throw new NotFoundException("Лайк от пользователя с id = {0} не найден.", userId);
        }
        filmStorage.save(film.toBuilder()
                .likes(likes)
                .build());
    }

    public List<Film> popularFilms(Long count) {
        return filmStorage.findAll().stream()
                .sorted(this::compareFilms)
                .limit(count)
                .collect(Collectors.toList());
    }

    private int compareFilms(Film f1, Film f2) {

        return f2.getLikes().size() - f1.getLikes().size();
    }
}
