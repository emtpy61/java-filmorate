package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreStorage;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.mappers.GenreMapper;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component("GenreDbStorage")
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Genre> genreMapper = new GenreMapper();

    public GenreDbStorage(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<Genre> findById(int id) {
        try {
            String SQL_FIND_GENRE = "select * from genres where id = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_FIND_GENRE, genreMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Genre> findAll() {
        String SQL_GET_ALL = "select * from genres";
        return jdbcTemplate.query(SQL_GET_ALL, genreMapper);
    }

    @Override
    public List<Genre> findAllByFilm(int filmId) {
        String SQL_GET_FILM_GENRES
                = "select genre_id from film_genres where film_id=? group by genre_id order by genre_id ASC";
        List<Integer> ids = jdbcTemplate.queryForList(SQL_GET_FILM_GENRES, Integer.class, filmId);

        return ids.stream()
                .map(this::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }


    @Override
    public void addFilmGenre(int filmId, int genreId) {
        String SQL_INSERT_FILM_GENRE = "insert into film_genres(film_id,genre_id) values (?,?) ";
        jdbcTemplate.update(SQL_INSERT_FILM_GENRE, filmId, genreId);
    }

    @Override
    public void deleteFilmGenre(int filmId) {
        String SQL_DELETE_FILM_GENRE = "delete from film_genres where film_id = ?";
        jdbcTemplate.update(SQL_DELETE_FILM_GENRE, filmId);
    }
}
