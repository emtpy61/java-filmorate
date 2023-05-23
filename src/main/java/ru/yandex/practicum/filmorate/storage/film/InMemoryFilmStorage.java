package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private long currentId = 0;

    @Override
    public Film save(Film film) {
        if (!films.containsKey(film.getId())) {
            film.setId(++currentId);
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Optional<Film> findById(Long id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public boolean existsById(Long id) {
        return films.containsKey(id);
    }

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public void deleteById(Long id) {
        films.remove(id);
    }

    @Override
    public void delete(Film film) {
        deleteById(film.getId());
    }

    @Override
    public void deleteAll() {
        films.clear();
    }
}
