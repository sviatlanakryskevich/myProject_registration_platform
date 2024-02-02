package com.tms.skv.registration_platform.service;

import com.tms.skv.registration_platform.entity.ClientEntity;
import com.tms.skv.registration_platform.entity.DoctorEntity;
import com.tms.skv.registration_platform.entity.OrderEntity;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface OrderEntityService {
    List<OrderEntity> getDoctorOrdersByTime(LocalDateTime from,LocalDateTime to, Integer doctorId);
    OrderEntity createOrder(DoctorEntity doctor, ClientEntity client, LocalDateTime appointmentTime);
}
