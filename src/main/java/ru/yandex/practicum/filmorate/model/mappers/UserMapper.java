package ru.yandex.practicum.filmorate.model.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new User(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("login"),
                Objects.requireNonNull(resultSet.getDate("birthday")).toLocalDate());
    }
}
