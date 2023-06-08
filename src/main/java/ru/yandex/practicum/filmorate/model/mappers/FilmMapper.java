package ru.yandex.practicum.filmorate.model.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class FilmMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new Film(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                Objects.requireNonNull(resultSet.getDate("release_date")).toLocalDate(),
                resultSet.getLong("duration"),
                new ArrayList<>(),
                new Mpa(resultSet.getInt("mpa_id"), "", ""));
    }
}