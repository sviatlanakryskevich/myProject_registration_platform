package com.tms.skv.registration_platform.web;

import com.tms.skv.registration_platform.domain.DoctorSpecialty;
import com.tms.skv.registration_platform.domain.Record;
import com.tms.skv.registration_platform.entity.DoctorEntity;
import com.tms.skv.registration_platform.entity.OrderEntity;
import com.tms.skv.registration_platform.service.impl.DoctorEntityServiceImpl;
import com.tms.skv.registration_platform.service.impl.OrderEntityServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@RequiredArgsConstructor
@Controller
@RequestMapping
public class AppointmentController {
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
        LocalTime startWeekTime = LocalTime.of(8, 0);
        LocalTime endWeekTime = LocalTime.of(17, 0);
        LocalDateTime from = LocalDateTime.of(monday, startWeekTime);
        LocalDateTime to = LocalDateTime.of(friday, endWeekTime);
        List<OrderEntity> ordersByWeek = orderEntityService.getDoctorOrdersByTime(from, to, doctorId);
        LocalTime dinnerBreak1= LocalTime.of(13, 0);
        LocalTime dinnerBreak2 = LocalTime.of(13, 30);
        Map<LocalTime, List<Record>> intervals = new LinkedHashMap<>();
        LocalTime start = dateTime.toLocalTime();

        for (int i = 1; i <= 18; i++) {
            if (!start.equals(dinnerBreak1) && !dateTime.equals(dinnerBreak2)) {

                intervals.put(start, currentWeek);
            }
            start = start.plusMinutes(30);

        }


        ModelAndView modelAndView = new ModelAndView("schedule");
        modelAndView.addObject("currentWeek", currentWeek);
        modelAndView.addObject("intervals", intervals);
        /*
        /*orderEntityService.getDoctorOrdersByTime();*/
        return modelAndView;
    }
    /*INSERT into doctors VALUES (2.0, 1, 'true','THERAPIST','doctor1');*/
}
