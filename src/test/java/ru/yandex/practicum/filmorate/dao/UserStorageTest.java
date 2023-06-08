package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserStorageTest {
    private final FilmDbStorage filmStorage;

    @Test
    public void testGetFilmById() {

        Film createFilm = new Film(1,
                "name",
                "description",
                LocalDate.now().minusYears(10),
                190,
                new ArrayList<>(),
                new Mpa(1, "name", "description"));
        filmStorage.create(createFilm);

        Film dbFilm = filmStorage.findById(1).get();
        assertThat(dbFilm).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    void getAllFilms() {
        Film first = new Film(1,
                "name",
                "description",
                LocalDate.now().minusYears(10),
                190,
                new ArrayList<>(),
                new Mpa(1, "name", "description"));
        Film second = new Film(2,
                "name2",
                "description2",
                LocalDate.now().minusYears(20),
                290,
                new ArrayList<>(),
                new Mpa(2, "name2", "description2"));
        filmStorage.create(first);
        filmStorage.create(second);

        Collection<Film> dbFilms = filmStorage.findAll();
        assertEquals(2, dbFilms.size());
    }

    @Test
    void updateFilm() {
        Film first = new Film(1,
                "name",
                "description",
                LocalDate.now().minusYears(10),
                190,
                new ArrayList<>(),
                new Mpa(1, "name", "description"));
        Film created = filmStorage.findById(filmStorage.create(first)).get();
        created.setName("update");
        filmStorage.update(created);
        Film dbFilm = filmStorage.findById(created.getId()).get();
        assertThat(dbFilm).hasFieldOrPropertyWithValue("name", "update");
    }

    @Test
    void deleteFilm() {
        Film first = new Film(1,
                "name",
                "description",
                LocalDate.now().minusYears(10),
                190,
                new ArrayList<>(),
                new Mpa(1, "name", "description"));
        Film second = new Film(2,
                "name2",
                "description2",
                LocalDate.now().minusYears(20),
                290,
                new ArrayList<>(),
                new Mpa(2, "name2", "description2"));
        Film addedFirst = filmStorage.findById(filmStorage.create(first)).get();
        Film addedSecond = filmStorage.findById(filmStorage.create(second)).get();

        Collection<Film> beforeDelete = filmStorage.findAll();
        filmStorage.deleteById(first.getId());
        Collection<Film> afterDelete = filmStorage.findAll();
        assertEquals(beforeDelete.size() - 1, afterDelete.size());
    }
}