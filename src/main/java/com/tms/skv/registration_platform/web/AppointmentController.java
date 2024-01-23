package com.tms.skv.registration_platform.web;

import com.tms.skv.registration_platform.domain.DoctorSpecialty;
import com.tms.skv.registration_platform.entity.DoctorEntity;
import com.tms.skv.registration_platform.service.impl.DoctorEntityServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping
public class AppointmentController {
    private final DoctorEntityServiceImpl doctorEntityService;
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
    /*INSERT into doctors VALUES (2.0, 1, 'true','THERAPIST','doctor1');*/
}
