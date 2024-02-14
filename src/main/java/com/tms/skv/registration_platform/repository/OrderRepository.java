package com.tms.skv.registration_platform.repository;

import com.tms.skv.registration_platform.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Integer>{
    List<OrderEntity> findByAppointmentTimeAfterAndAppointmentTimeBeforeAndDoctor_Id(LocalDateTime from, LocalDateTime to, Integer doctorId);
    List<OrderEntity> findByUser_Id(Integer userId);
    Optional<OrderEntity> findByAppointmentTimeAndDoctor_Id(LocalDateTime appointmentTime, Integer doctorId);
}
