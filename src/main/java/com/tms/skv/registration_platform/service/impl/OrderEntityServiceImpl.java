package com.tms.skv.registration_platform.service.impl;

import com.tms.skv.registration_platform.entity.OrderEntity;
import com.tms.skv.registration_platform.repository.OrderRepository;
import com.tms.skv.registration_platform.service.OrderEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
@Service
@RequiredArgsConstructor
public class OrderEntityServiceImpl implements OrderEntityService {
    private final OrderRepository orderRepository;
    @Override
    public List<OrderEntity> getDoctorOrdersByTime(LocalDateTime from, LocalDateTime to, Integer doctorId) {
        List<OrderEntity> ordersByTime = orderRepository.findByAppointmentTimeAfterAndAppointmentTimeBeforeAndAndDoctor_Id(from, to, doctorId);
        return ordersByTime;
    }

    @Override
    public OrderEntity createOrder(Integer doctorId, Integer clientId, LocalDateTime appointmentTime) {
        return null;
    }
}
