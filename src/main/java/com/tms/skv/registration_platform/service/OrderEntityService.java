package com.tms.skv.registration_platform.service;

import com.tms.skv.registration_platform.entity.OrderEntity;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface OrderEntityService {
    List<OrderEntity> getDoctorOrdersByTime(LocalDateTime from,LocalDateTime to, Integer doctorId);
    OrderEntity createOrder(Integer doctorId, Integer clientId, LocalDateTime appointmentTime);
}
