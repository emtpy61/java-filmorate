package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Objects;

import static ru.yandex.practicum.filmorate.exception.NotFoundException.notFoundException;

@Slf4j
@Service
@AllArgsConstructor
public class GenreService {
    private final GenreStorage genreStorage;

    public Genre findGenreById(int id) {
        return genreStorage.findById(id)
                .orElseThrow(notFoundException("Жанр с id = {0} не найден.", id));
    }

    public List<Genre> getGenres() {
        return genreStorage.findAll();
    }

    public void addGenresForCurrentFilm(Film film) {
        if (Objects.isNull(film.getGenres())) {
            return;
        }
        film.getGenres()
                .forEach(genre -> genreStorage.addFilmGenre(film.getId(), genre.getId()));
    }

    public void updateGenresForCurrentFilm(Film film) {
        genreStorage.deleteFilmGenre(film.getId());
        addGenresForCurrentFilm(film);
    }

    public void getGenreForCurrentFilm(Film film) {
        film.setGenres(genreStorage.findAllByFilm(film.getId()));
    }
}
