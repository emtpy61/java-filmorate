package ru.yandex.practicum.filmorate.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.filmorate.model.Users;


public interface UserRepository extends JpaRepository<Users, Long> {
}
