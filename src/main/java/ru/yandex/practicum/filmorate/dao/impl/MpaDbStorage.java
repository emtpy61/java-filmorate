package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaStorage;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.mappers.MpaMapper;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Component("MpaDbStorage")
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Mpa> mpaRowMapper = new MpaMapper();

    public MpaDbStorage(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<Mpa> findById(int id) {
        try {
            String SQL_FIND_MPA = "select * from mpa where id = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_FIND_MPA, mpaRowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Mpa> findAll() {
        String SQL_GET_ALL = "select * from mpa";
        return jdbcTemplate.query(SQL_GET_ALL, mpaRowMapper);
    }
}
