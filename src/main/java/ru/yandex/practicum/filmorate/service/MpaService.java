package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

import static ru.yandex.practicum.filmorate.exception.NotFoundException.notFoundException;

@Slf4j
@Service
@AllArgsConstructor
public class MpaService {
    private final MpaStorage mpaStorage;

    public Mpa findMpaById(int id) {
        return mpaStorage.findById(id)
                .orElseThrow(notFoundException("Рейтинг МРА с id = {0} не найден.", id));
    }

    public List<Mpa> getMpa() {
        return mpaStorage.findAll();
    }

    public void addMpaToFilm(Film film) {
        film.setMpa(
                findMpaById(
                        film.getMpa().getId()
                )
        );
    }
}
