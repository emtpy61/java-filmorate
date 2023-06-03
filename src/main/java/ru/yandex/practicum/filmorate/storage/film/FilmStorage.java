package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Film save(Film film);

    Optional<Film> findById(Long id);

    boolean existsById(Long id);

    List<Film> findAll();

    void deleteById(Long id);

    void delete(Film film);

    void deleteAll();
}
