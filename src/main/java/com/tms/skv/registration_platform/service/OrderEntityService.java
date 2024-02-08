package com.tms.skv.registration_platform.service;

import com.tms.skv.registration_platform.entity.UserEntity;
import com.tms.skv.registration_platform.entity.DoctorEntity;
import com.tms.skv.registration_platform.entity.OrderEntity;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderEntityService {
    List<OrderEntity> getDoctorOrdersByTime(LocalDateTime from,LocalDateTime to, Integer doctorId);
    OrderEntity createOrder(DoctorEntity doctor, UserEntity client, LocalDateTime appointmentTime);
    List<OrderEntity> getOrdersByUser(Integer userId);

    void deleteOrder(Integer orderId);

}
