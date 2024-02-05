package com.tms.skv.registration_platform.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime appointmentTime;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    @ToString.Exclude
    private UserEntity client;

    @ManyToOne
    @JoinColumn(name="doctor_id", nullable=false)
    @ToString.Exclude
    private DoctorEntity doctor;
}
