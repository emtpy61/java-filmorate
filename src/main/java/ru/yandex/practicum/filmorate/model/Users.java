package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}
