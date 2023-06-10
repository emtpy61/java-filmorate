package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaStorage;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.mappers.MpaMapper;

import java.util.List;
import java.util.Optional;

@Component("MpaDbStorage")
@AllArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaMapper mpaRowMapper;

    @Override
    public Optional<Mpa> findById(int id) {
        try {
            String sqlFindMpa = "select * from mpa where id = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlFindMpa, mpaRowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Mpa> findAll() {
        String sqlGetAll = "select * from mpa";
        return jdbcTemplate.query(sqlGetAll, mpaRowMapper);
    }
}
