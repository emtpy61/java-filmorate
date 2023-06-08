package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {
    Optional<Genre> findById(int id);

    List<Genre> findAll();

    List<Genre> findAllByFilm(int filmId);

    void addFilmGenre(int filmId, int genreId);

    void deleteFilmGenre(int filmId);
}
