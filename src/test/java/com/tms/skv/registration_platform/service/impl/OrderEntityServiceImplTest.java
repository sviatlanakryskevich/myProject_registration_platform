package com.tms.skv.registration_platform.service.impl;

import com.tms.skv.registration_platform.domain.DoctorSpecialty;
import com.tms.skv.registration_platform.domain.Sex;
import com.tms.skv.registration_platform.entity.DoctorEntity;
import com.tms.skv.registration_platform.entity.OrderEntity;
import com.tms.skv.registration_platform.entity.UserEntity;
import com.tms.skv.registration_platform.exc.AppointmentIsExistException;
import com.tms.skv.registration_platform.exc.NotFoundException;
import com.tms.skv.registration_platform.repository.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderEntityServiceImplTest {
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private OrderEntityServiceImpl orderEntityService;

    @Test
    void testGetDoctorOrdersByTime() {
        UserEntity user = UserEntity.builder()
                .id(1)
                .login("sviatlana")
                .password("$2a$10$oArt.zTpia2sqFq1pkHPMuYJrMMfw77l3MSkPsoXO0DAoyQTZ9UyS")
                .address("Torunska")
                .email("sviatlana.skiba@gmail.com")
                .birthday(LocalDate.of(1996, 8, 8))
                .firstName("Sviatlana")
                .lastName("Kryskevich")
                .sex(Sex.WOMAN)
                .phoneNumber("+48787916247")
                .perm("ROLE_USER")
                .build();
        DoctorEntity doctor = DoctorEntity.builder()
                .doctorSpecialty(DoctorSpecialty.CARDIOLOGIST)
                .id(1)
                .experience(2.0)
                .fullName("Сойко Вера Владимировна")
                .build();
        OrderEntity order1 = OrderEntity.builder()
                .doctor(doctor)
                .appointmentTime(LocalDateTime.of(2024, 2, 21, 11, 0))
                .id(1)
                .user(user)
                .build();
        OrderEntity order2 = OrderEntity.builder()
                .doctor(doctor)
                .appointmentTime(LocalDateTime.of(2024, 2, 22, 9, 30))
                .id(2)
                .user(user)
                .build();

        LocalDateTime from = LocalDateTime.of(2024,2,20, 8, 0);
        LocalDateTime to = LocalDateTime.of(2024,2,22, 17, 0);

        when(orderRepository.findByAppointmentTimeAfterAndAppointmentTimeBeforeAndDoctor_Id(from, to, doctor.getId()))
                .thenReturn(List.of(order1, order2));

        List<OrderEntity> all = orderEntityService.getDoctorOrdersByTime(from, to, doctor.getId());

        Assertions.assertThat(all).hasSize(2);
        Assertions.assertThat(all).contains(order1, order2);
    }


    @Test
    void testCreateOrder() {
        LocalDateTime appointmentTime = LocalDateTime.of(2024, 2, 21, 11, 0);
        UserEntity user = UserEntity.builder()
                .id(1)
                .login("sviatlana")
                .password("$2a$10$oArt.zTpia2sqFq1pkHPMuYJrMMfw77l3MSkPsoXO0DAoyQTZ9UyS")
                .address("Torunska")
                .email("sviatlana.skiba@gmail.com")
                .birthday(LocalDate.of(1996, 8, 8))
                .firstName("Sviatlana")
                .lastName("Kryskevich")
                .sex(Sex.WOMAN)
                .phoneNumber("+48787916247")
                .perm("ROLE_USER")
                .build();
        DoctorEntity doctor = DoctorEntity.builder()
                .doctorSpecialty(DoctorSpecialty.CARDIOLOGIST)
                .id(1)
                .experience(2.0)
                .fullName("Сойко Вера Владимировна")
                .build();
        OrderEntity order = OrderEntity.builder()
                .doctor(doctor)
                .appointmentTime(appointmentTime)
                .user(user)
                .build();
        when(orderRepository.findByAppointmentTimeAndDoctor_Id(appointmentTime, doctor.getId()))
                .thenReturn(Optional.empty());
        when(orderRepository.save(order)).thenReturn(order);
        OrderEntity order1 = orderEntityService.createOrder(doctor, user, appointmentTime);
        Mockito.verify(orderRepository).save(order);
        Assertions.assertThat(order1.getAppointmentTime()).isEqualTo(order.getAppointmentTime());
        Assertions.assertThat(order1.getDoctor()).isEqualTo(order.getDoctor());
        Assertions.assertThat(order1.getUser()).isEqualTo(order.getUser());
    }

    @Test
    void testCreateOrderThrowException() {
        LocalDateTime appointmentTime = LocalDateTime.of(2024, 2, 21, 11, 0);
        UserEntity user = UserEntity.builder()
                .id(1)
                .login("sviatlana")
                .password("$2a$10$oArt.zTpia2sqFq1pkHPMuYJrMMfw77l3MSkPsoXO0DAoyQTZ9UyS")
                .address("Torunska")
                .email("sviatlana.skiba@gmail.com")
                .birthday(LocalDate.of(1996, 8, 8))
                .firstName("Sviatlana")
                .lastName("Kryskevich")
                .sex(Sex.WOMAN)
                .phoneNumber("+48787916247")
                .perm("ROLE_USER")
                .build();
        DoctorEntity doctor = DoctorEntity.builder()
                .doctorSpecialty(DoctorSpecialty.CARDIOLOGIST)
                .id(1)
                .experience(2.0)
                .fullName("Сойко Вера Владимировна")
                .build();
        OrderEntity order = OrderEntity.builder()
                .id(1)
                .doctor(doctor)
                .appointmentTime(appointmentTime)
                .user(user)
                .build();
        when(orderRepository.findByAppointmentTimeAndDoctor_Id(appointmentTime, doctor.getId()))
                .thenReturn(Optional.of(order));
        Assertions.assertThatThrownBy(() -> orderEntityService.createOrder(doctor, user, appointmentTime)).isInstanceOf(AppointmentIsExistException.class);
    }

    @Test
    void getOrdersByUser() {
        UserEntity user = UserEntity.builder()
                .id(1)
                .login("sviatlana")
                .password("$2a$10$oArt.zTpia2sqFq1pkHPMuYJrMMfw77l3MSkPsoXO0DAoyQTZ9UyS")
                .address("Torunska")
                .email("sviatlana.skiba@gmail.com")
                .birthday(LocalDate.of(1996, 8, 8))
                .firstName("Sviatlana")
                .lastName("Kryskevich")
                .sex(Sex.WOMAN)
                .phoneNumber("+48787916247")
                .perm("ROLE_USER")
                .build();
        OrderEntity order1 = OrderEntity.builder()
                .id(1)
                .doctor(new DoctorEntity())
                .appointmentTime(LocalDateTime.of(2024, 2, 21, 11, 0))
                .user(user)
                .build();
        OrderEntity order2 = OrderEntity.builder()
                .id(2)
                .doctor(new DoctorEntity())
                .appointmentTime(LocalDateTime.of(2024, 2, 20, 16, 30))
                .user(user)
                .build();
        when(orderRepository.findByUser_Id(user.getId()))
                .thenReturn(List.of(order1, order2));
        List<OrderEntity> all = orderEntityService.getOrdersByUser(user.getId());

        Assertions.assertThat(all).hasSize(2);
        Assertions.assertThat(all).contains(order1, order2);
    }

    @Test
    void testDeleteOrder() {
        OrderEntity order = OrderEntity.builder()
                .id(1)
                .doctor(new DoctorEntity())
                .appointmentTime(LocalDateTime.now())
                .user(new UserEntity())
                .build();

        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        orderEntityService.deleteOrder(1);
        Mockito.verify(orderRepository).delete(order);
    }

    @Test
    void testDeleteThrowException() {
        when(orderRepository.findById(2)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> orderEntityService.deleteOrder(2)).isInstanceOf(NotFoundException.class);
        Mockito.verify(orderRepository, Mockito.times(0)).delete(Mockito.any());
    }

}