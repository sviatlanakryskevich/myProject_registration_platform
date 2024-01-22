package com.tms.skv.registration_platform.model;

import com.tms.skv.registration_platform.domain.DoctorSpecialty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDto {
    private Integer id;
    private String fullName;
    private DoctorSpecialty doctorSpecialty;
    private Double experience;
    private boolean isKnowledgeOfEnglish;
}
