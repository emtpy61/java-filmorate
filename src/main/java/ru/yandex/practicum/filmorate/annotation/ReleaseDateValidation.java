package ru.yandex.practicum.filmorate.annotation;

import ru.yandex.practicum.filmorate.validation.ReleaseDateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Constraint(validatedBy = ReleaseDateValidator.class)
public @interface ReleaseDateValidation {
    String message() default "не может быть раньше 28 Декабря 1895г.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
