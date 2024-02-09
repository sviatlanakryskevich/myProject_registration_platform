package com.tms.skv.registration_platform.model;

import com.tms.skv.registration_platform.domain.Sex;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {
    private Integer id;
    private String username;
    private String password;
    @NotBlank(message = "Обязательное поле")
    @Size(min = 2, max = 12, message = "Поле должно содержать от 2 до 12 символов")
    private String firstName;
    @NotBlank(message = "Обязательное поле")
    @Size(min = 2, max = 12, message = "Поле должно содержать от 2 до 12 символов")
    private String lastName;
    @NotNull(message = "Обязательное поле")
    private Sex sex;
    private String address;
    @PastOrPresent(message = "Введите валидную дату рождения")
    @NotNull(message = "Обязательное поле")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    @NotBlank(message = "Обязательное поле")
    private String phoneNumber;
    @NotBlank (message = "Обязательное поле")
    @Email(message = "Адрес электронной почты должен быть валидным")
    private String email;
}
