package com.tms.skv.registration_platform.web;

import com.tms.skv.registration_platform.domain.DoctorSpecialty;
import com.tms.skv.registration_platform.domain.Record;
import com.tms.skv.registration_platform.domain.Sex;
import com.tms.skv.registration_platform.entity.UserEntity;
import com.tms.skv.registration_platform.entity.DoctorEntity;
import com.tms.skv.registration_platform.entity.OrderEntity;
import com.tms.skv.registration_platform.mapper.UserMapper;
import com.tms.skv.registration_platform.model.UserDto;
import com.tms.skv.registration_platform.service.impl.DoctorEntityServiceImpl;
import com.tms.skv.registration_platform.service.impl.OrderEntityServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
@RequestMapping
public class OrderController {
    private final DoctorEntityServiceImpl doctorEntityService;
    private final OrderEntityServiceImpl orderEntityService;
    
    @GetMapping("/main")
    public ModelAndView mainPage() {
        ModelAndView modelAndView = new ModelAndView("main");
        modelAndView.addObject("specialties", DoctorSpecialty.values());
        return modelAndView;
    }

    @GetMapping("/get/{specialty}")
    public ModelAndView getDoctorsBySpecialty(@PathVariable(name = "specialty") DoctorSpecialty specialty) {
        ModelAndView modelAndView = new ModelAndView("specialty");
        List<DoctorEntity> doctorsBySpecialty = doctorEntityService.findBySpecialty(specialty);
        modelAndView.addObject("doctorsBySpecialty", doctorsBySpecialty);
        return modelAndView;
    }

    @GetMapping("/schedule/{id}")
    public ModelAndView getSchedule(@PathVariable(name = "id") Integer doctorId, LocalDateTime dateTime) {
        if (dateTime == null) {
            LocalDate date = LocalDate.now();
            LocalTime time = LocalTime.of(8, 0);
            dateTime = LocalDateTime.of(date, time);
        }

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
        /*LocalTime startWeekTime = LocalTime.of(8, 0);*/
        LocalTime endWeekTime = LocalTime.of(17, 0);
        LocalTime startWeekTime = dateTime.toLocalTime();
        LocalDateTime from = LocalDateTime.of(monday, startWeekTime);
        LocalDateTime to = LocalDateTime.of(friday, endWeekTime);

        List<OrderEntity> ordersByWeek = orderEntityService.getDoctorOrdersByTime(from, to, doctorId);
        List<LocalDateTime> onRecordTime = ordersByWeek.stream()
                .map(orderEntity -> orderEntity.getAppointmentTime())
                .collect(Collectors.toList());

        LocalTime dinnerBreak1 = LocalTime.of(13, 0);
        LocalTime dinnerBreak2 = LocalTime.of(13, 30);
        Map<LocalTime, List<Record>> intervals = new LinkedHashMap<>();


        for (int i = 1; i <= 18; i++) {
            if (!startWeekTime.equals(dinnerBreak1) && !dateTime.equals(dinnerBreak2)) {
                ArrayList<Record> records = new ArrayList<>();
                for (LocalDate day : currentWeek) {
                    LocalDateTime slot = LocalDateTime.of(day, startWeekTime);
                    boolean isOrdered = onRecordTime.contains(slot);
                    Record record = new Record(slot, isOrdered, doctorId);
                    records.add(record);
                }

                intervals.put(startWeekTime, records);
            }
            startWeekTime = startWeekTime.plusMinutes(30);

        }
        DoctorEntity doctor = doctorEntityService.findById(doctorId);
        ModelAndView modelAndView = new ModelAndView("schedule");
        modelAndView.addObject("currentWeek", currentWeek);
        modelAndView.addObject("intervals", intervals);
        modelAndView.addObject("doctor", doctor);
        return modelAndView;
    }

    @PostMapping("/createOrder")
    public ModelAndView createOrder(@RequestParam(value = "doctorId") Integer doctorId,
                                    @RequestParam(value = "appointment") LocalDateTime appointment){
        DoctorEntity doctor = doctorEntityService.findById(doctorId);
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        OrderEntity savedOrder = orderEntityService.createOrder(doctor, user, appointment);
        ModelAndView modelAndView = new ModelAndView("order");
        modelAndView.addObject("savedOrder", savedOrder);
        return modelAndView;
    }

    @GetMapping("/getOrders")
    public ModelAndView getOrders(){
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer id = user.getId();
        List<OrderEntity> ordersByUser = orderEntityService.getOrdersByUser(id);
        ModelAndView modelAndView = new ModelAndView("myOrders");
        modelAndView.addObject("orders", ordersByUser);
        return modelAndView;
    }

    @GetMapping("/deleteOrder")
    public ModelAndView deleteOrder(@RequestParam(value = "id") Integer orderId){
        ModelAndView modelAndView = new ModelAndView("myOrders");
        orderEntityService.deleteOrder(orderId);
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer id = user.getId();
        List<OrderEntity> ordersByUser = orderEntityService.getOrdersByUser(id);
        modelAndView.addObject("orders", ordersByUser);
        return modelAndView;
    }
}
