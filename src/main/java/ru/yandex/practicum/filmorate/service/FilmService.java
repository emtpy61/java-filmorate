package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

import static ru.yandex.practicum.filmorate.exception.NotFoundException.notFoundException;

@Slf4j
@Service
@AllArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final MpaService mpaService;
    private final GenreService genreService;


    public Film addFilm(Film film) {
        film.setId(filmStorage.create(film));
        genreService.addGenresForCurrentFilm(film);
        fillNames(film);
        return film;
    }

    public Film findFilmById(int id) {
        Film film = filmStorage.findById(id)
                .orElseThrow(notFoundException("Фильм с id = {0} не найден.", id));
        fillNames(film);
        return film;
    }

    public List<Film> getFilms() {
        List<Film> films = filmStorage.findAll();
        films.forEach(this::fillNames);
        return films;
    }

    public Film updateFilm(Film film) {
        checkFilmExists(film.getId());
        genreService.updateGenresForCurrentFilm(film);
        fillNames(film);
        filmStorage.update(film);
        fillNames(film);
        return film;
    }

    public void deleteFilmById(int id) {
        checkFilmExists(id);
        filmStorage.deleteById(id);
    }

    public void clearFilms() {
        filmStorage.deleteAll();
    }

    public void addLike(int filmId, int userId) {
        checkFilmExists(filmId);
        checkUserExists(userId);
        filmStorage.addLike(filmId, userId);
    }

    public void deleteLike(int filmId, int userId) {
        checkFilmExists(filmId);
        checkUserExists(userId);
        filmStorage.deleteLike(filmId, userId);
    }

    public List<Film> popularFilms(int count) {
        List<Film> films = filmStorage.getPopularFilms(count);
        films.forEach(this::fillNames);
        return films;
    }

    private void checkFilmExists(int id) {
        if (filmStorage.notExistsById(id)) {
            throw new NotFoundException("Фильм с id = {0} не найден.", id);
        }
    }

    private void checkUserExists(int id) {
        if (userStorage.notExistsById(id)) {
            throw new NotFoundException("Пользователь с id = {0} не найден.", id);
        }
    }

    private void fillNames(Film film) {
        mpaService.addMpaToFilm(film);
        genreService.getGenreForCurrentFilm(film);
    }
}
