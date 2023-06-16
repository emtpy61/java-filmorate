package ru.yandex.practicum.filmorate.model.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MpaMapper implements RowMapper<Mpa> {
    @Override
    public Mpa mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new Mpa(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("description"));
    }
}