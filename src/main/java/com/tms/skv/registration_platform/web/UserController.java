package com.tms.skv.registration_platform.web;

import com.tms.skv.registration_platform.domain.Sex;
import com.tms.skv.registration_platform.entity.OrderEntity;
import com.tms.skv.registration_platform.entity.UserEntity;
import com.tms.skv.registration_platform.model.UserDto;
import com.tms.skv.registration_platform.service.UserEntityService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping
public class UserController {
    private final UserEntityService userEntityService;
    @GetMapping("/public")
    public ModelAndView publicPage(UserDto userDto){
        ModelAndView modelAndView = new ModelAndView("public");
        return modelAndView;
    }

    @GetMapping("/logout")
    public ModelAndView logout(){
        ModelAndView modelAndView = new ModelAndView("public");
        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView save(@Valid UserDto userDto, BindingResult result){
        if(!result.hasFieldErrors()){
            if(userDto.getId() !=null){
                userEntityService.update(userDto);
                String success = "Редактирование сохранено.";
                ModelAndView modelAndView = new ModelAndView("register");
                modelAndView.addObject("sexes", Sex.values());
                modelAndView.addObject("message", success);
                return modelAndView;
            }else{
                userEntityService.save(userDto);
                ModelAndView modelAndView = new ModelAndView("public");
                modelAndView.addObject("sexes", Sex.values());
                return modelAndView;
            }
        }else {
            ModelAndView modelAndView = new ModelAndView("register");
            modelAndView.addObject("sexes", Sex.values());
            return modelAndView;
        }
    }

    @GetMapping("/register")
    public ModelAndView registerPage(UserDto userDto){
        ModelAndView modelAndView = new ModelAndView("register");
        modelAndView.addObject("sexes", Sex.values());
        return modelAndView;
    }


}
