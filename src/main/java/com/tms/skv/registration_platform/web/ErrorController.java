package com.tms.skv.registration_platform.web;

import com.tms.skv.registration_platform.domain.DoctorSpecialty;
import com.tms.skv.registration_platform.exc.AppointmentIsExistException;
import com.tms.skv.registration_platform.exc.NotFoundException;
import com.tms.skv.registration_platform.exc.NotUniqueUserNameException;
import com.tms.skv.registration_platform.model.UserDto;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ErrorController {
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFoundException(NotFoundException exc){
        String errorMessage = exc.getMessage();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("error",errorMessage);
        return modelAndView;
    }

    @ExceptionHandler(NotUniqueUserNameException.class)
    public ModelAndView handleNotUniqueUserNameException(NotUniqueUserNameException exc) {
        ModelAndView modelAndView = new ModelAndView("register");
        modelAndView.addObject("userDto", exc.getUser());
        modelAndView.addObject("errorMessage", exc.getMessage());
        return modelAndView;
    }
}
