package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UsersTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    private static Users user;

    @BeforeAll
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
        user = Users.builder()
                .login("dolore")
                .name("Nick Name")
                .email("mail@mail.ru")
                .birthday(LocalDate.now())
                .build();
    }

    @AfterAll
    public static void close() {
        validatorFactory.close();
    }

    @Test
    void shouldCreateUser() {
        Set<ConstraintViolation<Users>> violations = validator.validate(user);

        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    void shouldNotCreateUserIfLoginIsWrong() {
        String[] logins = {"dolore ullamco", "d olore ullamc o", "", " ", null};

        Arrays.stream(logins).forEach(login -> {
            Users userWithIncorrectLogin = user
                    .toBuilder()
                    .login(login)
                    .build();

            Set<ConstraintViolation<Users>> violations = validator.validate(userWithIncorrectLogin);

            Assertions.assertFalse(violations.isEmpty());
        });
    }

    @Test
    void shouldNotCreateUserIfEmailIsWrong() {
        String[] emails = {"mail @mail.ru", "mail@ mail.ru", ".mail@mail.ru", "@mail.ru", "mail@","it-wrong?email@",
                "", " ", null};

        Arrays.stream(emails).forEach(email -> {
            Users userWithIncorrectEmail = user
                    .toBuilder()
                    .email(email)
                    .build();

            Set<ConstraintViolation<Users>> violations = validator.validate(userWithIncorrectEmail);

            Assertions.assertFalse(violations.isEmpty());
        });
    }

    @Test
    void shouldNotCreateUserIfBirthdayIsWrong() {
        Users userWithIncorrectBirthday = user
                .toBuilder()
                .birthday(LocalDate.now().plusDays(1))
                .build();

        Set<ConstraintViolation<Users>> violations = validator.validate(userWithIncorrectBirthday);

        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());
    }

}