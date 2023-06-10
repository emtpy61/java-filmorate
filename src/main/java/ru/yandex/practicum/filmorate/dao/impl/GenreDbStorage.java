package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreStorage;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.mappers.GenreMapper;

import java.util.Collection;
import java.util.Optional;

@Component("GenreDbStorage")
@AllArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreMapper genreMapper;

    @Override
    public Optional<Genre> findById(int id) {
        try {
            String sqlFindGenre = "select * from genres where id = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlFindGenre, genreMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Collection<Genre> findAll() {
        String sqlGetAll = "select * from genres";
        return jdbcTemplate.query(sqlGetAll, genreMapper);
    }
}
