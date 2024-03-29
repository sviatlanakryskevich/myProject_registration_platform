package com.tms.skv.registration_platform.web;

import com.tms.skv.registration_platform.domain.Sex;
import com.tms.skv.registration_platform.entity.UserEntity;
import com.tms.skv.registration_platform.mapper.UserMapper;
import com.tms.skv.registration_platform.model.UserDto;
import com.tms.skv.registration_platform.model.UserUpdateDto;
import com.tms.skv.registration_platform.repository.UserRepository;
import com.tms.skv.registration_platform.service.UserEntityService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
@RequestMapping
public class UserController {
    private final UserEntityService userEntityService;
    private final UserMapper userMapper;
    @GetMapping("/login")
    public ModelAndView loginPage(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("login");
        if (request.getParameter("error") != null) {
            String errorMessage = "Неправильный логин или/и пароль ";
            modelAndView.addObject("errorMessage", errorMessage);
        }
        return modelAndView;
    }

    @GetMapping("/logout")
    public ModelAndView logout(){
        return new ModelAndView("login");
    }

    @PostMapping("/register")
    public ModelAndView save(@Valid UserDto userDto, BindingResult result) {
        if (!result.hasFieldErrors()) {
            userEntityService.save(userDto);
            return new ModelAndView("login");
        } else {
            ModelAndView modelAndView = new ModelAndView("register");
            modelAndView.addObject("sexes", Sex.values());
            return modelAndView;
        }
    }



    @GetMapping("/register")
    public ModelAndView registerPage(){
        ModelAndView modelAndView = new ModelAndView("register");
        modelAndView.addObject("sexes", Sex.values());
        modelAndView.addObject("userDto", new UserDto());
        return modelAndView;
    }

    @GetMapping("/updateUser")
    public ModelAndView updateFormPage() {
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        UserEntity userEntity = userEntityService.getByUsername(username);
        UserUpdateDto updateDto = userMapper.toUpdateDto(userEntity);
        ModelAndView modelAndView = new ModelAndView("updateUser");
        modelAndView.addObject("sexes", Sex.values());
        modelAndView.addObject("userUpdateDto", updateDto);
        return modelAndView;
    }

    @PostMapping("/updateUser")
    public ModelAndView updateUser(@Valid UserUpdateDto userDto, BindingResult result){
        ModelAndView modelAndView = new ModelAndView("updateUser");
        modelAndView.addObject("sexes", Sex.values());
        if(!result.hasFieldErrors()){
                userEntityService.update(userDto);
                String success = "Редактирование сохранено";
                modelAndView.addObject("message", success);
        }
        return modelAndView;
    }
}
