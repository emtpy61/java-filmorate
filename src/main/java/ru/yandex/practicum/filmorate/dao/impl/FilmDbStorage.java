package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.mappers.FilmMapper;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Film> filmMapper = new FilmMapper();

    public FilmDbStorage(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public int create(Film film) {
        String SQL_INSERT_FILM = "insert into films("
                + "name, description, release_date, duration,mpa_id) values(?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    SQL_INSERT_FILM, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, film.getName());
            preparedStatement.setString(2, film.getDescription());
            preparedStatement.setDate(3, Date.valueOf(film.getReleaseDate()));
            preparedStatement.setLong(4, film.getDuration());
            preparedStatement.setInt(5, film.getMpa().getId());
            return preparedStatement;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    @Override
    public boolean notExistsById(int id) {
        String SQL_EXISTS_FILM = "select 1 from films where id = ?";
        return !jdbcTemplate.queryForRowSet(SQL_EXISTS_FILM, id).next();
    }

    @Override
    public Optional<Film> findById(int id) {
        try {
            String SQL_FIND_FILM = "select * from films where id = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_FIND_FILM, filmMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Film> findAll() {
        String SQL_GET_ALL = "select * from films";
        return jdbcTemplate.query(SQL_GET_ALL, filmMapper);
    }

    @Override
    public void update(Film film) {
        String SQL_UPDATE_FILM = "update films "
                + "set name = ?, description = ?, release_date  = ?, duration  = ?, mpa_id  = ? where id = ?";
        jdbcTemplate.update(SQL_UPDATE_FILM,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
    }

    @Override
    public void deleteById(int id) {
        String SQL_DELETE_FILM = "delete from films where id = ?";
        jdbcTemplate.update(SQL_DELETE_FILM, id);
    }

    @Override
    public void deleteAll() {
        String SQL_DELETE_ALL = "delete from films";
        jdbcTemplate.execute(SQL_DELETE_ALL);
    }

    @Override
    public void addLike(int filmId, int userId) {
        String SQL_INSERT_USER_LIKE = "insert into film_likes(film_id, user_id) values(?,?)";
        jdbcTemplate.update(SQL_INSERT_USER_LIKE, filmId, userId);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        String SQL_GET_POPULAR = "SELECT films.*, COUNT(l.film_id) as count FROM films " +
                "LEFT JOIN film_likes l ON films.id=l.film_id " +
                "GROUP BY films.id " +
                "ORDER BY count DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(SQL_GET_POPULAR, filmMapper, count);
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        String SQL_DELETE_USER_LIKE = "delete from film_likes where film_id = ? and user_id = ?";
        jdbcTemplate.update(SQL_DELETE_USER_LIKE, filmId, userId);
    }
}


