package com.tms.skv.registration_platform.web;

import com.tms.skv.registration_platform.domain.Sex;
import com.tms.skv.registration_platform.model.UserDto;
import com.tms.skv.registration_platform.repository.ClientRepository;
import com.tms.skv.registration_platform.service.DBUserDetailsService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
@RequestMapping
public class ClientController {
    private final DBUserDetailsService userService;
    private final ClientRepository clientRepository;
    @GetMapping("/public")
    public ModelAndView publicPage(UserDto userDto){
        ModelAndView modelAndView = new ModelAndView("public");
        return modelAndView;
    }
    @GetMapping("/main")
    public String mainPage(){
        return "main";
    }

        @GetMapping("/welcome")
    public String logout(){
        return "welcome";
    }

    @PostMapping("/register")
    public ModelAndView save(@Valid UserDto userDto, BindingResult result){
        ModelAndView modelAndView = new ModelAndView("public");
        modelAndView.addObject("sexes", Sex.values());
        ModelAndView modelAndView1 = new ModelAndView("register");
        if(!result.hasFieldErrors()){
            userService.save(userDto);
            return modelAndView;
        }else {
            return modelAndView1;
        }
        /*userService.save(user);
        return modelAndView;*/
    }

    @GetMapping("/register")
    public ModelAndView registerPage(UserDto userDto){
        ModelAndView modelAndView = new ModelAndView("register");
        modelAndView.addObject("sexes", Sex.values());
        return modelAndView;
    }
}
