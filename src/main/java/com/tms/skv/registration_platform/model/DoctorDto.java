package com.tms.skv.registration_platform.model;

import com.tms.skv.registration_platform.domain.DoctorSpecialty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorDto {
    private Integer id;
    @NotBlank(message = "Обязательное поле")
    @Size(min = 6, message = "Поле должно содержать минимально 6 символов")
    private String fullName;
    @NotNull(message = "Обязательное поле")
    private DoctorSpecialty doctorSpecialty;
    @NotNull(message = "Обязательное поле")
    private Double experience;
}
