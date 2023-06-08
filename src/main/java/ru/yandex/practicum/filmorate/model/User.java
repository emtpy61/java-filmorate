package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class User {

    private int id;
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
}
