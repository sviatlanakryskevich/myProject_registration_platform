package com.tms.skv.registration_platform.service.impl;

import com.tms.skv.registration_platform.domain.Record;
import com.tms.skv.registration_platform.entity.UserEntity;
import com.tms.skv.registration_platform.entity.DoctorEntity;
import com.tms.skv.registration_platform.entity.OrderEntity;
import com.tms.skv.registration_platform.exc.AppointmentIsExistException;
import com.tms.skv.registration_platform.exc.NotFoundException;
import com.tms.skv.registration_platform.repository.OrderRepository;
import com.tms.skv.registration_platform.service.OrderEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderEntityServiceImpl implements OrderEntityService {
    private final OrderRepository orderRepository;
    @Override
    public List<OrderEntity> getDoctorOrdersByTime(LocalDateTime from, LocalDateTime to, Integer doctorId) {
        return orderRepository.findByAppointmentTimeAfterAndAppointmentTimeBeforeAndDoctor_Id(from, to, doctorId);
    }

    @Override
    public OrderEntity createOrder(DoctorEntity doctor, UserEntity user, LocalDateTime appointmentTime) {
        Integer id = doctor.getId();
        Optional<OrderEntity> optionalOrder = orderRepository.findByAppointmentTimeAndDoctor_Id(appointmentTime, id);
        if(optionalOrder.isEmpty()){
            OrderEntity order = OrderEntity.builder()
                    .appointmentTime(appointmentTime)
                    .user(user)
                    .doctor(doctor)
                    .build();
            return orderRepository.save(order);
        } else {
            throw new AppointmentIsExistException("Запись уже недоступна, пожалуйста перезагрузите страницу");
        }


    }

    @Override
    @Transactional
    public List<OrderEntity> getOrdersByUser(Integer userId){
        return orderRepository.findByUser_Id(userId);
    }

    @Override
    @Transactional
    public void deleteOrder(Integer orderId) {
        Optional<OrderEntity> orderOpt = orderRepository.findById(orderId);
        if(orderOpt.isPresent()){
            OrderEntity order = orderOpt.get();
            orderRepository.delete(order);
        }else {
            throw new NotFoundException("Заказ с таким id не найден");
        }
    }

    public List<LocalDate> getCurrentWeek(LocalDateTime dateTime) {
        LocalDate monday = dateTime.toLocalDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate tuesday = monday.plusDays(1);
        LocalDate wednesday = tuesday.plusDays(1);
        LocalDate thursday = wednesday.plusDays(1);
        LocalDate friday = thursday.plusDays(1);

        List<LocalDate> currentWeek = new ArrayList<>();
        currentWeek.add(monday);
        currentWeek.add(tuesday);
        currentWeek.add(wednesday);
        currentWeek.add(thursday);
        currentWeek.add(friday);
        return currentWeek;
    }

    public Map<LocalTime, List<Record>> getTimeSlotsMap(Integer doctorId, List<LocalDate> currentWeek) {
        LocalDateTime now = LocalDateTime.now();
        LocalTime endWeekTime = LocalTime.of(17, 0);
        LocalTime startWeekTime = LocalTime.of(8, 0);
        LocalDateTime from = LocalDateTime.of(currentWeek.get(0), startWeekTime);
        LocalDateTime to = LocalDateTime.of(currentWeek.get(4), endWeekTime);

        List<OrderEntity> ordersByWeek = getDoctorOrdersByTime(from, to, doctorId);
        List<LocalDateTime> onRecordTime = ordersByWeek.stream()
                .map(OrderEntity::getAppointmentTime)
                .toList();

        LocalTime time = startWeekTime;
        LocalTime dinnerBreak1 = LocalTime.of(13, 0);
        LocalTime dinnerBreak2 = LocalTime.of(13, 30);
        Map<LocalTime, List<Record>> intervals = new LinkedHashMap<>();


        for (int i = 1; i <= 18; i++) {
            if (!time.equals(dinnerBreak1) && !time.equals(dinnerBreak2)) {
                ArrayList<Record> records = new ArrayList<>();
                for (LocalDate day : currentWeek) {
                    LocalDateTime slot = LocalDateTime.of(day, time);
                    boolean isUnavailable = onRecordTime.contains(slot) || slot.isBefore(now) || slot.minusWeeks(2).isAfter(now);
                    Record record = new Record(slot, isUnavailable, doctorId);
                    records.add(record);
                }

                intervals.put(time, records);
            }
            time = time.plusMinutes(30);

        }
        return intervals;
    }
}
