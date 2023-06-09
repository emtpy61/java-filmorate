package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.mappers.UserMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component("UserDbStorage")
@AllArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;

    @Override
    public User create(User user) {
        String sqlInsertUser = "insert into users(name, email, login, birthday) values(?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    sqlInsertUser, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getLogin());
            preparedStatement.setDate(4, Date.valueOf(user.getBirthday()));

            return preparedStatement;
        }, keyHolder);

        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return user;
    }

    @Override
    public boolean notExistsById(int id) {
        String sqlExistsUser = "select 1 from users where id = ?";
        return !jdbcTemplate.queryForRowSet(sqlExistsUser, id).next();
    }

    @Override
    public Optional<User> findById(int id) {
        try {
            String sqlFindUser = "select * from users where id = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlFindUser, userMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAll() {
        String sqlGetAll = "select * from users";
        return jdbcTemplate.query(sqlGetAll, userMapper);
    }

    @Override
    public List<User> findAllById(List<Integer> ids) {
        String inParams = String.join(",", Collections.nCopies(ids.size(), "?"));
        String sqlFindUsersById = String.format("select * from users where id in (%s)", inParams);
        return jdbcTemplate.query(sqlFindUsersById, userMapper, ids.toArray());
    }

    @Override
    public User update(User user) {
        String sqlUpdateUser = "update users "
                + "set name = ?, email = ?, login  = ?, birthday  = ? where id = ?";
        jdbcTemplate.update(sqlUpdateUser,
                user.getName(), user.getEmail(), user.getLogin(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public void deleteById(int id) {
        String sqlDeleteUser = "delete from users where id = ?";
        jdbcTemplate.update(sqlDeleteUser, id);
    }

    @Override
    public void deleteAll() {
        String sqlDeleteAll = "delete from users";
        jdbcTemplate.execute(sqlDeleteAll);
    }

    @Override
    public void addFriend(int id, int friendId) {
        String sqlInsertUserFriend = "insert into friendship(user_id, friend_id) values(?,?)";
        jdbcTemplate.update(sqlInsertUserFriend, id, friendId);
    }

    @Override
    public List<Integer> getFriendsIds(int id) {
        String sqlFindUserFriend = "select friend_id from friendship where user_id = ?";
        return jdbcTemplate.queryForList(sqlFindUserFriend, Integer.class, id);
    }

    @Override
    public void deleteFriend(int id, int friendId) {
        String sqlDeleteUserFriend = "delete from friendship where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sqlDeleteUserFriend, id, friendId);
    }
}

