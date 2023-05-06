package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FilmTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    private static Film film;

    @BeforeAll
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
        film = Film.builder()
                .id(1L)
                .name("nisi eiusmod")
                .description("adipisicing")
                .releaseDate(LocalDate.parse("1967-03-25"))
                .duration(100L)
                .build();
    }

    @AfterAll
    public static void close() {
        validatorFactory.close();
    }

    @Test
    void shouldCreateFilm() {
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    void shouldCreateFilmWithFirstFilmDate() {
        Film filmWithFirstFilmDate = film
                .toBuilder()
                .releaseDate(LocalDate.parse("1895-12-28"))
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(filmWithFirstFilmDate);

        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    void shouldNotCreateFilmIfNameIsEmpty() {
        String[] names = {"", " ", "  ", null};

        Arrays.stream(names).forEach(name -> {
            Film filmWithIncorrectName = film
                    .toBuilder()
                    .name(name)
                    .build();

            Set<ConstraintViolation<Film>> violations = validator.validate(filmWithIncorrectName);

            Assertions.assertFalse(violations.isEmpty());
            Assertions.assertEquals(1, violations.size());
        });
    }

    @Test
    void shouldCreateFilmIfDescription200Chars() {
        Film filmWithIncorrectDescription = film
                .toBuilder()
                .description("a".repeat(200))
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(filmWithIncorrectDescription);

        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    void shouldNotCreateFilmIfDescriptionTooLong() {
        Film filmWithIncorrectDescription = film
                .toBuilder()
                .description("a".repeat(201))
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(filmWithIncorrectDescription);

        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());
    }

    @Test
    void shouldNotCreateFilmIfReleaseDateIsWrong() {
        String[] dates = {"1895-12-27", "2100-12-27"};

        Arrays.stream(dates).forEach(date -> {
            Film filmWithIncorrectReleaseDate = film
                    .toBuilder()
                    .releaseDate(LocalDate.parse(date))
                    .build();

            Set<ConstraintViolation<Film>> violations = validator.validate(filmWithIncorrectReleaseDate);

            Assertions.assertFalse(violations.isEmpty());
            Assertions.assertEquals(1, violations.size());
        });
    }

    @Test
    void shouldNotCreateFilmIfDurationIsWrong() {
        Film filmWithIncorrectDuration = film
                .toBuilder()
                .duration(-100)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(filmWithIncorrectDuration);

        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());
    }

}