package com.tms.skv.registration_platform.service.impl;

import com.tms.skv.registration_platform.entity.UserEntity;
import com.tms.skv.registration_platform.entity.DoctorEntity;
import com.tms.skv.registration_platform.entity.OrderEntity;
import com.tms.skv.registration_platform.repository.OrderRepository;
import com.tms.skv.registration_platform.service.OrderEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public OrderEntity createOrder(DoctorEntity doctor, UserEntity client, LocalDateTime appointmentTime) {
        OrderEntity order = OrderEntity.builder()
                .appointmentTime(appointmentTime)
                .client(client)
                .doctor(doctor)
                .build();
        OrderEntity save = orderRepository.save(order);
        return save;
    }

    @Override
    public List<OrderEntity> getOrdersByUser(){
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<OrderEntity> orders = user.getOrders();
        return orders;
    }
}
