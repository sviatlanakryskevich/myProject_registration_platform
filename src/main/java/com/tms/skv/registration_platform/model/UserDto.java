package com.tms.skv.registration_platform.model;

import com.tms.skv.registration_platform.domain.Sex;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private Sex sex;
    private String address;
    private LocalDate birthday;
    private String phoneNumber;
    private String email;
}
