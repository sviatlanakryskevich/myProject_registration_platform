package com.tms.skv.registration_platform.web;

import com.tms.skv.registration_platform.domain.Sex;
import com.tms.skv.registration_platform.exc.NotFoundException;
import com.tms.skv.registration_platform.exc.NotUniqueUserNameException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ErrorController {
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFoundException(NotFoundException exc){
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("error",exc.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(NotUniqueUserNameException.class)
    public ModelAndView handleNotUniqueUserNameException(NotUniqueUserNameException exc) {
        ModelAndView modelAndView = new ModelAndView("register");
        modelAndView.addObject("userDto", exc.getUser());
        modelAndView.addObject("sexes", Sex.values());
        modelAndView.addObject("errorMessage", exc.getMessage());
        return modelAndView;
    }
}
