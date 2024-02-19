package com.tms.skv.registration_platform.entity;

import com.tms.skv.registration_platform.domain.DoctorSpecialty;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    @OneToMany(mappedBy = "doctor")
    @ToString.Exclude
    private List<OrderEntity> orders;

}
