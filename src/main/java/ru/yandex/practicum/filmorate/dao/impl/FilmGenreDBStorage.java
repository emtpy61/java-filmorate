package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmGenreStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.mappers.GenreMapper;

import java.sql.PreparedStatement;
import java.util.*;
import java.util.stream.Collectors;

@Component("FilmGenreDBStorage")
@AllArgsConstructor
public class FilmGenreDBStorage implements FilmGenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFilmGenres(Film film) {
        Set<Genre> genres = new LinkedHashSet<>(film.getGenres());
        String sqlInsertFilmGenre = "insert into film_genres(film_id,genre_id) values (?,?) ";
        jdbcTemplate.batchUpdate(sqlInsertFilmGenre, genres, genres.size(),
                (PreparedStatement ps, Genre genre) -> {
                    ps.setInt(1, film.getId());
                    ps.setInt(2, genre.getId());
                }
        );
    }

    @Override
    public void deleteFilmGenre(Film film) {
        String sqlDeleteFilmGenre = "delete from film_genres where film_id = ?";
        jdbcTemplate.update(sqlDeleteFilmGenre, film.getId());
    }

    @Override
    public Collection<Genre> findAllByFilmId(int filmId) {
        final String sql = "select g.id as id, name "
                + "from film_genres fg "
                + "left join genres g on fg.genre_id = g.id "
                + "where film_id = ?";

        return jdbcTemplate.query(sql, new GenreMapper(), filmId);
    }

    @Override
    public Map<Integer, Collection<Genre>> getAllFilmGenres(Collection<Film> films) {
        String inParams = films.stream()
                .map(Film::getId)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        String sql = "select fg.film_id as film_id, "
                + "g.id as genre_id, "
                + "g.name as name "
                + "from film_genres fg "
                + "left join genres g on fg.genre_id = g.id "
                + "where fg.film_id in (%s)";
        String sqlGetAllFilmGenres = String.format(sql, inParams);

        Map<Integer, Collection<Genre>> filmGenresMap = new HashMap<>();

        jdbcTemplate.query(sqlGetAllFilmGenres, rs -> {
            Genre genre = new Genre(rs.getInt("genre_id"), rs.getString("name"));

            Integer filmId = rs.getInt("film_id");

            filmGenresMap.putIfAbsent(filmId, new ArrayList<>());
            filmGenresMap.get(filmId).add(genre);
        });

        return filmGenresMap;
    }
}
