package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final UserDbStorage userDbStorage;
    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    public void beforeEach() {
        user1 = new User(1,
                "Name1",
                "email@mail.ru",
                "Login1",
                LocalDate.of(2023, 1, 1));
        user2 = new User(2,
                "Name2",
                "email2@mail.ru",
                "Login2",
                LocalDate.of(2023, 1, 2));
        user3 = new User(3,
                "Name3",
                "email3@mail.ru",
                "Login3",
                LocalDate.of(2023, 1, 3));
    }

    @Test
    public void testGetUserById() {
        userDbStorage.create(user1);
        User createdUser = userDbStorage.findById(1).get();
        assertThat(createdUser).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    void getAllUsers() {
        userDbStorage.create(user2);
        userDbStorage.create(user3);
        List<User> createdUsers = userDbStorage.findAll();
        assertEquals(2, createdUsers.size());
    }

    @Test
    void deleteUser() {
        Collection<User> beforeDelete = userDbStorage.findAll();
        userDbStorage.deleteById(user1.getId());
        Collection<User> afterDelete = userDbStorage.findAll();
        assertEquals(beforeDelete.size() - 1, afterDelete.size());
    }
}

