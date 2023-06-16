package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

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

    public Collection<Genre> getGenres() {
        return genreStorage.findAll();
    }
}
