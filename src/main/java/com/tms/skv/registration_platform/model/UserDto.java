package com.tms.skv.registration_platform.model;

import com.tms.skv.registration_platform.domain.Sex;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @NotBlank(message = "Обязательное поле")
    @Size(min = 6, max = 12, message = "Поле должно содержать от 6 до 12 символов")
    private String username;
    @NotBlank(message = "Обязательное поле")
    @Size(min = 6, max = 12, message = "Поле должно содержать от 6 до 12 символов")
    private String password;
    @NotBlank(message = "Обязательное поле")
    @Size(min = 2, max = 12, message = "Поле должно содержать от 6 до 12 символов")
    private String firstName;
    @NotBlank(message = "Обязательное поле")
    @Size(min = 2, max = 12, message = "Поле должно содержать от 6 до 12 символов")
    private String lastName;
    @NotBlank(message = "Обязательное поле")
    private Sex sex;
    private String address;
    @NotBlank(message = "Обязательное поле")
    @PastOrPresent(message = "Введите валидную дату рождения")
    private LocalDate birthday;
    @NotBlank(message = "Обязательное поле")
    private String phoneNumber;
    @NotBlank (message = "Обязательное поле")
    @Email(message = "Адрес электронной почты должен быть валидным")
    private String email;
}
