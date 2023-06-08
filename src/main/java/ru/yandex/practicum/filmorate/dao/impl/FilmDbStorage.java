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
        String sqlInsertFilm = "insert into films("
                + "name, description, release_date, duration,mpa_id) values(?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    sqlInsertFilm, Statement.RETURN_GENERATED_KEYS);
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
        String sqlExistsFilm = "select 1 from films where id = ?";
        return !jdbcTemplate.queryForRowSet(sqlExistsFilm, id).next();
    }

    @Override
    public Optional<Film> findById(int id) {
        try {
            String sqlFindFilm = "select * from films where id = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlFindFilm, filmMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Film> findAll() {
        String sqlGetAll = "select * from films";
        return jdbcTemplate.query(sqlGetAll, filmMapper);
    }

    @Override
    public void update(Film film) {
        String sqlUpdateFilm = "update films "
                + "set name = ?, description = ?, release_date  = ?, duration  = ?, mpa_id  = ? where id = ?";
        jdbcTemplate.update(sqlUpdateFilm,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
    }

    @Override
    public void deleteById(int id) {
        String sqlDeleteFilm = "delete from films where id = ?";
        jdbcTemplate.update(sqlDeleteFilm, id);
    }

    @Override
    public void deleteAll() {
        String sqlDeleteAll = "delete from films";
        jdbcTemplate.execute(sqlDeleteAll);
    }

    @Override
    public void addLike(int filmId, int userId) {
        String sqlInsertUserLike = "insert into film_likes(film_id, user_id) values(?,?)";
        jdbcTemplate.update(sqlInsertUserLike, filmId, userId);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        String sqlGetPopular = "select films.*, count(l.film_id) as count from films " +
                "left join film_likes l on films.id=l.film_id " +
                "group by films.id " +
                "order by count desc " +
                "limit ?";
        return jdbcTemplate.query(sqlGetPopular, filmMapper, count);
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        String sqlDeleteUserLike = "delete from film_likes where film_id = ? and user_id = ?";
        jdbcTemplate.update(sqlDeleteUserLike, filmId, userId);
    }
}


