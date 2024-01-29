package com.tms.skv.registration_platform.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date appointmentTime;

    @ManyToOne
    @JoinColumn(name="client_id", nullable=false)
    @ToString.Exclude
    private ClientEntity client;

    @ManyToOne
    @JoinColumn(name="doctor_id", nullable=false)
    @ToString.Exclude
    private DoctorEntity doctor;
}
