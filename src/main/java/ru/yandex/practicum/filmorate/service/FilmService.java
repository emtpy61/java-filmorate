package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmGenreStorage;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;

import static ru.yandex.practicum.filmorate.exception.NotFoundException.notFoundException;

@Slf4j
@Service
@AllArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final FilmGenreStorage filmGenreStorage;

    public Film addFilm(Film film) {
        film.setId(filmStorage.create(film));
        addFilmGenres(film);
        return setFilmGenres(film);
    }

    public Film findFilmById(int id) {
        Film film = filmStorage.findById(id)
                .orElseThrow(notFoundException("Фильм с id = {0} не найден.", id));
        return setFilmGenres(film);
    }

    public Collection<Film> getFilms() {
        Collection<Film> films = filmStorage.findAll();
        return setFilmsGenres(films);
    }

    public Film updateFilm(Film film) {
        checkFilmExists(film.getId());
        filmStorage.update(film);
        updateFilmGenres(film);
        return setFilmGenres(film);
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

    public Collection<Film> popularFilms(int count) {
        List<Film> films = filmStorage.getPopularFilms(count);
        return setFilmsGenres(films);
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

    private Collection<Film> setFilmsGenres(Collection<Film> films) {
        Map<Integer, Collection<Genre>> filmGenresMap = filmGenreStorage.getAllFilmGenres(films);

        films.forEach(film -> {
            int filmId = film.getId();
            film.setGenres(filmGenresMap.getOrDefault(filmId, new ArrayList<>()));
        });

        return films;
    }

    private Film setFilmGenres(Film film) {
        film.setGenres(filmGenreStorage.findAllByFilmId(film.getId()));
        return film;
    }

    private void addFilmGenres(Film film) {
        if (Objects.isNull(film.getGenres())) {
            return;
        }
        filmGenreStorage.addFilmGenres(film);
    }

    private void updateFilmGenres(Film film) {
        filmGenreStorage.deleteFilmGenre(film);
        addFilmGenres(film);
    }

    private void getGenreForFilm(Film film) {
        film.setGenres(filmGenreStorage.findAllByFilmId(film.getId()));
    }
}
