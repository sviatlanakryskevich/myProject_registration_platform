package com.tms.skv.registration_platform.web;

import com.tms.skv.registration_platform.domain.DoctorSpecialty;
import com.tms.skv.registration_platform.domain.Record;
import com.tms.skv.registration_platform.entity.UserEntity;
import com.tms.skv.registration_platform.entity.DoctorEntity;
import com.tms.skv.registration_platform.entity.OrderEntity;
import com.tms.skv.registration_platform.exc.AppointmentIsExistException;
import com.tms.skv.registration_platform.service.impl.DoctorEntityServiceImpl;
import com.tms.skv.registration_platform.service.impl.OrderEntityServiceImpl;
import com.tms.skv.registration_platform.service.impl.UserEntityServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;


@RequiredArgsConstructor
@Controller
@RequestMapping
public class OrderController {
    private final DoctorEntityServiceImpl doctorEntityService;
    private final OrderEntityServiceImpl orderEntityService;
    private final UserEntityServiceImpl userEntityService;

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
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.of(8, 0);
        LocalDateTime now = LocalDateTime.of(date, time);
        modelAndView.addObject("doctorsBySpecialty", doctorsBySpecialty);
        modelAndView.addObject("now", now);
        return modelAndView;
    }

    @GetMapping("/schedule/{id}")
    public ModelAndView getSchedule(@PathVariable(name = "id") Integer doctorId,
                                    @RequestParam(value = "dateTime") LocalDateTime dateTime) {
        DoctorEntity doctor = doctorEntityService.findById(doctorId);
        LocalDateTime next = dateTime.plusWeeks(1);
        LocalDateTime previous = dateTime.minusWeeks(1);

        List<LocalDate> currentWeek = orderEntityService.getCurrentWeek(dateTime);

        Map<LocalTime, List<Record>> intervals = orderEntityService.getTimeSlotsMap(doctorId, currentWeek);

        ModelAndView modelAndView = new ModelAndView("schedule");
        modelAndView.addObject("currentWeek", currentWeek);
        modelAndView.addObject("intervals", intervals);
        modelAndView.addObject("doctor", doctor);
        modelAndView.addObject("next", next);
        modelAndView.addObject("previous", previous);
        return modelAndView;
    }

    @PostMapping("/createOrder")
    public ModelAndView createOrder(@RequestParam(value = "doctorId") Integer doctorId,
                                    @RequestParam(value = "appointment") LocalDateTime appointment) {
        DoctorEntity doctor = doctorEntityService.findById(doctorId);

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        UserEntity user = userEntityService.getByUsername(username);
        ModelAndView modelAndView = new ModelAndView("order");
        modelAndView.addObject("doctorId", doctorId);
        modelAndView.addObject("now", LocalDateTime.now());
        try {
            OrderEntity savedOrder = orderEntityService.createOrder(doctor, user, appointment);
            modelAndView.addObject("savedOrder", savedOrder);
        } catch (AppointmentIsExistException e){
            modelAndView.addObject("errorMsg", e.getMessage());
        }
        return modelAndView;
    }

    @GetMapping("/getOrders")
    public ModelAndView getOrders() {
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        UserEntity userEntity = userEntityService.getByUsername(username);
        Integer id = userEntity.getId();
        List<OrderEntity> ordersByUser = orderEntityService.getOrdersByUser(id);
        LocalDateTime today = LocalDateTime.now();

        ModelAndView modelAndView = new ModelAndView("myOrders");
        modelAndView.addObject("orders", ordersByUser);
        modelAndView.addObject("today", today);
        return modelAndView;
    }

    @GetMapping("/deleteOrder")
    public ModelAndView deleteOrder(@RequestParam(value = "id") Integer orderId) {
        ModelAndView modelAndView = new ModelAndView("myOrders");
        orderEntityService.deleteOrder(orderId);
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        UserEntity userEntity = userEntityService.getByUsername(username);
        Integer id = userEntity.getId();
        List<OrderEntity> ordersByUser = orderEntityService.getOrdersByUser(id);
        modelAndView.addObject("orders", ordersByUser);
        modelAndView.addObject("today", LocalDateTime.now());
        return modelAndView;
    }
}
