package com.tms.skv.registration_platform.web;

import com.tms.skv.registration_platform.exc.NotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorController {
    @ExceptionHandler(NotFoundException.class)
    public String getError(NotFoundException exc){
        String errorMessage = exc.getErrorMessage();
        System.out.println(errorMessage);
        return "notFoundError";
    }
}
