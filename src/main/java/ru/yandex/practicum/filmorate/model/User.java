package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class User {

    private long id;
    private String name;

    @NotBlank(message = "не может быть пустым.")
    @Email(message = "не является адресом электронной почты.")
    private String email;
    @NotBlank(message = "не может быть пустым.")
    @Pattern(regexp = "^\\S*$", message = "не может содержать пробелы.")
    private String login;

    @NotNull(message = "не может быть null.")
    @PastOrPresent(message = "не может быть в будущем.")
    private LocalDate birthday;

    @Singular
    private Set<Long> friends = new HashSet<>();
}
