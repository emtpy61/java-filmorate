package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreDbStorageTest {

    private final GenreDbStorage genreDbStorage;

    @Test
    public void testGetGenreById() {
        Genre genre = genreDbStorage.findById(1).get();
        assertThat(genre).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    void testGetAllGenres() {
        Collection<Genre> genres = genreDbStorage.findAll();
        assertEquals(6, genres.size());
    }
}