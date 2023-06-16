DROP ALL OBJECTS;

CREATE TABLE IF NOT EXISTS mpa (
    id          INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name        VARCHAR(40),
    description VARCHAR(200)
);

CREATE TABLE IF NOT EXISTS films (
    id            INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    description   VARCHAR(200) NOT NULL,
    release_date  TIMESTAMP    NOT NULL,
    duration      INTEGER         NOT NULL,
    mpa_id        INTEGER REFERENCES mpa (id) ON DELETE RESTRICT
);



CREATE TABLE IF NOT EXISTS genres (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name     VARCHAR(40) NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
    id  INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email    VARCHAR(50) NOT NULL,
    login    VARCHAR(50) NOT NULL,
    name     VARCHAR(50),
    birthday DATE        NOT NULL
);

CREATE TABLE IF NOT EXISTS film_genres (

    film_id  INTEGER NOT NULL REFERENCES films (id) ON DELETE CASCADE,
    genre_id INTEGER NOT NULL REFERENCES genres (id) ON DELETE RESTRICT,
    PRIMARY KEY (film_id,genre_id)
);

CREATE TABLE IF NOT EXISTS film_likes (
    film_id INTEGER NOT NULL REFERENCES films (id) ON DELETE CASCADE,
    user_id INTEGER NOT NULL REFERENCES users (id),
    PRIMARY KEY (film_id,user_id)
);

CREATE TABLE IF NOT EXISTS friendship (
    user_id         INTEGER NOT NULL REFERENCES users (id),
    friend_id       INTEGER NOT NULL REFERENCES users (id),
    PRIMARY KEY (user_id,friend_id)
);

