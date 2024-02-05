package com.tms.skv.registration_platform.web;

import com.tms.skv.registration_platform.domain.DoctorSpecialty;
import com.tms.skv.registration_platform.domain.Sex;
import com.tms.skv.registration_platform.entity.DoctorEntity;
import com.tms.skv.registration_platform.model.DoctorDto;
import com.tms.skv.registration_platform.model.UserDto;
import com.tms.skv.registration_platform.service.impl.DoctorEntityServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping
public class AdministrationController {
    private final DoctorEntityServiceImpl doctorEntityService;

    @GetMapping("/admin")
    public ModelAndView getAllDoctors() {
        ModelAndView modelAndView = new ModelAndView("admin");
        List<DoctorEntity> allDoctors = doctorEntityService.findAll();
        modelAndView.addObject("allDoctors", allDoctors);
        return modelAndView;
    }

    @GetMapping("/createDoctor")
    public ModelAndView createFormPage(DoctorDto doctorDto) {
        ModelAndView modelAndView = new ModelAndView("createDoctor");
        modelAndView.addObject("specialties", DoctorSpecialty.values());
        return modelAndView;
    }

    @PostMapping("/createDoctor")
    public ModelAndView create(@Valid DoctorDto doctorDto, BindingResult result) {
        if (!result.hasFieldErrors()) {
            ModelAndView modelAndView = new ModelAndView("admin");
            doctorEntityService.save(doctorDto);
            List<DoctorEntity> allDoctors = doctorEntityService.findAll();
            modelAndView.addObject("allDoctors", allDoctors);
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("createDoctor");
            modelAndView.addObject("specialties", DoctorSpecialty.values());
            return modelAndView;
        }
    }
}
