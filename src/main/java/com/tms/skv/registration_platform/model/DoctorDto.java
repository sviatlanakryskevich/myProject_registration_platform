package com.tms.skv.registration_platform.model;

import com.tms.skv.registration_platform.domain.DoctorSpecialty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDto {
    private Integer id;
    @NotBlank(message = "Обязательное поле")
    @Size(min = 6, max = 12, message = "Поле должно содержать от 6 до 12 символов")
    private String fullName;
    @NotNull(message = "Обязательное поле")
    private DoctorSpecialty doctorSpecialty;
    @NotNull(message = "Обязательное поле")
    private Double experience;
}
