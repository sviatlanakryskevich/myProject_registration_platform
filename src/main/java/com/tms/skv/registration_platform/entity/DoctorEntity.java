package com.tms.skv.registration_platform.entity;

import com.tms.skv.registration_platform.domain.DoctorSpecialty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "doctors")
public class DoctorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String fullName;
    @Enumerated(EnumType.STRING)
    private DoctorSpecialty doctorSpecialty;
    private Double experience;
    private boolean isKnowledgeOfEnglish;

}
