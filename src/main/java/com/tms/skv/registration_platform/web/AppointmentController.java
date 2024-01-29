package com.tms.skv.registration_platform.web;

import com.tms.skv.registration_platform.domain.DoctorSpecialty;
import com.tms.skv.registration_platform.entity.DoctorEntity;
import com.tms.skv.registration_platform.service.impl.DoctorEntityServiceImpl;
import com.tms.skv.registration_platform.service.impl.OrderEntityServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping
public class AppointmentController {
    private final DoctorEntityServiceImpl doctorEntityService;
    private final OrderEntityServiceImpl orderEntityService;
    @GetMapping("/main")
    public ModelAndView mainPage(){
        ModelAndView modelAndView = new ModelAndView("main");
        modelAndView.addObject("specialties", DoctorSpecialty.values());
        return modelAndView;
    }

    @GetMapping("/get/{specialty}")
    public ModelAndView getDoctorsBySpecialty(@PathVariable(name = "specialty") DoctorSpecialty specialty){
        ModelAndView modelAndView = new ModelAndView("specialty");
        List<DoctorEntity> doctorsBySpecialty = doctorEntityService.findBySpecialty(specialty);
        modelAndView.addObject("doctorsBySpecialty", doctorsBySpecialty);
        return  modelAndView;
    }
    @GetMapping("/schedule/{id}")
    public ModelAndView getSchedule(@PathVariable(name = "id") Integer doctorId, Date dateTime){
        if(dateTime == null) {
            dateTime = new Date();
            dateTime.setHours(8);
            dateTime.setMinutes(0);
            /*LocalDate date = LocalDate.now();
            LocalTime time = LocalTime.of(8, 0);
            dateTime = LocalDateTime.of(date, time);*/

        }
        /*LocalDateTime localDateMinus = dateTime.minusWeeks(1);
        LocalDateTime localDatePlus = dateTime.plusWeeks(1);*/
        ModelAndView modelAndView = new ModelAndView("schedule");
        modelAndView.addObject("timeNow", dateTime);
        /*orderEntityService.getDoctorOrdersByTime();*/
        return  modelAndView;
    }
    /*INSERT into doctors VALUES (2.0, 1, 'true','THERAPIST','doctor1');*/
}
