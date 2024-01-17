package com.tms.skv.registration_platform.web;

import com.tms.skv.registration_platform.domain.Sex;
import com.tms.skv.registration_platform.model.UserDto;
import com.tms.skv.registration_platform.repository.ClientRepository;
import com.tms.skv.registration_platform.service.DBUserDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
@Controller
@RequestMapping
public class ClientController {
    private final DBUserDetailsService userService;
    private final ClientRepository clientRepository;
    @GetMapping("/public")
    public String publicPage(){
        return "public";
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
    public String save(@Valid @ModelAttribute(name = "client")UserDto user, BindingResult result){
        /*if(!result.hasFieldErrors()){
            userService.save(user);
            return "public";
        }else {
            return "register";
        }*/
        userService.save(user);
        return "public";
    }

    @GetMapping("/register")
    public ModelAndView registerPage(@ModelAttribute(name = "client")UserDto user){
        ModelAndView modelAndView = new ModelAndView("register");
        modelAndView.addObject("sexes", Sex.values());
        return modelAndView;
    }
}
