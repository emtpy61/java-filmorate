package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaDbStorageTest {

    private final MpaDbStorage mpaDbStorage;

    @Test
    public void testGetGenreById() {
        Mpa mpa = mpaDbStorage.findById(1).get();
        assertThat(mpa).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    void testGetAllGenres() {
        List<Mpa> mpa = mpaDbStorage.findAll();
        assertEquals(5, mpa.size());
    }
}