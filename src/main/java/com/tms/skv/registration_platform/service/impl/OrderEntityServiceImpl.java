package com.tms.skv.registration_platform.service.impl;

import com.tms.skv.registration_platform.entity.UserEntity;
import com.tms.skv.registration_platform.entity.DoctorEntity;
import com.tms.skv.registration_platform.entity.OrderEntity;
import com.tms.skv.registration_platform.repository.OrderRepository;
import com.tms.skv.registration_platform.repository.UserRepository;
import com.tms.skv.registration_platform.service.OrderEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    public OrderEntity createOrder(DoctorEntity doctor, UserEntity user, LocalDateTime appointmentTime) {
        OrderEntity order = OrderEntity.builder()
                .appointmentTime(appointmentTime)
                .user(user)
                .doctor(doctor)
                .build();
        OrderEntity save = orderRepository.save(order);
        return save;
    }

    @Override
    @Transactional
    public List<OrderEntity> getOrdersByUser(Integer userId){
        List<OrderEntity> orders = orderRepository.findByUser_Id(userId);
        return orders;
    }

    @Override
    @Transactional
    public void deleteOrder(Integer orderId) {
        orderRepository.deleteById(orderId);
    }
}
