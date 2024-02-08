package com.tms.skv.registration_platform.web;

import com.tms.skv.registration_platform.exc.NotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ErrorController {
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView getError(NotFoundException exc){
        String errorMessage = exc.getMessage();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("error",errorMessage);
        return modelAndView;
    }
}
