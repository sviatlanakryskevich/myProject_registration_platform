package com.tms.skv.registration_platform.repository;

import com.tms.skv.registration_platform.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Integer>{
    List<OrderEntity> findByAppointmentTimeAfterAndAppointmentTimeBeforeAndAndDoctor_Id(LocalDateTime from, LocalDateTime to, Integer doctorId);
    List<OrderEntity> findByUser_Id(Integer userId);
}
