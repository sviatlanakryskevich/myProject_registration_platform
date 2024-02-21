package com.tms.skv.registration_platform.web;

import com.tms.skv.registration_platform.domain.DoctorSpecialty;
import com.tms.skv.registration_platform.entity.DoctorEntity;
import com.tms.skv.registration_platform.mapper.DoctorMapper;
import com.tms.skv.registration_platform.model.DoctorDto;
import com.tms.skv.registration_platform.service.impl.DoctorEntityServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping
public class AdministrationController {
    private final DoctorEntityServiceImpl doctorEntityService;
    private final DoctorMapper doctorMapper;

    @GetMapping("/admin")
    public ModelAndView getAllDoctors() {
        ModelAndView modelAndView = new ModelAndView("admin");
        List<DoctorEntity> allDoctors = doctorEntityService.findAll();
        modelAndView.addObject("allDoctors", allDoctors);
        return modelAndView;
    }

    @GetMapping("/createDoctor")
    public ModelAndView createFormPage() {
        ModelAndView modelAndView = new ModelAndView("createDoctor");
        modelAndView.addObject("specialties", DoctorSpecialty.values());
        modelAndView.addObject("doctorDto", new DoctorDto());
        return modelAndView;
    }

    @GetMapping("/updateDoctor")
    public ModelAndView updateFormPage(@RequestParam(value = "doctorId")Integer doctorId) {
        DoctorEntity doctor = doctorEntityService.findById(doctorId);
        DoctorDto dto = doctorMapper.toDto(doctor);
        ModelAndView modelAndView = new ModelAndView("createDoctor");
        modelAndView.addObject("specialties", DoctorSpecialty.values());
        modelAndView.addObject("doctorDto", dto);
        return modelAndView;
    }

    @PostMapping("/createDoctor")
    public ModelAndView saveDoctor(@Valid @RequestBody DoctorDto doctorDto, BindingResult result) {
        if (!result.hasFieldErrors()) {
            ModelAndView modelAndView = new ModelAndView("admin");
            if(doctorDto.getId() !=null){
                doctorEntityService.update(doctorDto);
            }else{
                doctorEntityService.save(doctorDto);
            }
            List<DoctorEntity> allDoctors = doctorEntityService.findAll();
            modelAndView.addObject("allDoctors", allDoctors);
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("createDoctor");
            modelAndView.addObject("specialties", DoctorSpecialty.values());
            return modelAndView;
        }
    }

    @GetMapping("/deleteDoctor")
    public ModelAndView delete(@RequestParam(value = "doctorId")Integer doctorId){
        ModelAndView modelAndView = new ModelAndView("admin");
        doctorEntityService.delete(doctorId);
        List<DoctorEntity> allDoctors = doctorEntityService.findAll();
        modelAndView.addObject("allDoctors", allDoctors);
        return modelAndView;
    }
}
