package com.tms.skv.registration_platform.web;

import com.tms.skv.registration_platform.domain.Sex;
import com.tms.skv.registration_platform.model.UserDto;
import com.tms.skv.registration_platform.repository.ClientRepository;
import com.tms.skv.registration_platform.service.DBUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    /*@PostMapping("/login"){
        public ModelAndView mainPage(@RequestParam(name = "login") String login, @RequestParam(name = "cred") String password)){
            clientRepository.findByLogin(login).ifPresent( clientEntity-> {
                if(clientEntity.getPassword().equals(password)){
                    String token = tokenService.createToken(userEntity);
                    response.setHeader("jwt-token", token);
                }
            })
            return "main";
        }
    }*/

    @GetMapping("/welcome")
    public String logout(){
        return "welcome";
    }

    @PostMapping("/register")
    public String save(UserDto user){
        userService.save(user);
        return "public";
    }

    @GetMapping("/register")
    public ModelAndView registerPage(){
        ModelAndView modelAndView = new ModelAndView("register");
        modelAndView.addObject("sexes", Sex.values());
        return modelAndView;
    }
}
