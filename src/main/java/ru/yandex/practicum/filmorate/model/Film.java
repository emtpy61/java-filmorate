package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.annotation.ReleaseDateValidation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Collection;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class Film {

    private int id;
    @NotNull(message = "не может быть null.")
    @NotBlank(message = "не должно быть пустым.")
    private String name;
    @Size(max = 200, message = "не должно превышать 200 символов.")
    private String description;
    @ReleaseDateValidation
    private LocalDate releaseDate;
    @Positive(message = "не может быть отрицательным.")
    private long duration;
    @Singular
    private Collection<Genre> genres;
    private Mpa mpa;
}
