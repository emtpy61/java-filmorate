package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    int create(Film film);

    boolean notExistsById(int id);

    Optional<Film> findById(int id);

    List<Film> findAll();

    void update(Film film);

    void deleteById(int id);

    void deleteAll();

    void addLike(int filmId, int userId);

    List<Film> getPopularFilms(int count);

    void deleteLike(int filmId, int userId);
}
