package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Map;

public interface FilmGenreStorage {

    void addFilmGenres(Film film);

    void deleteFilmGenre(Film film);

    Collection<Genre> findAllByFilmId(int filmId);

    Map<Integer, Collection<Genre>> getAllFilmGenres(Collection<Film> films);
}
