package com.tms.skv.registration_platform.web;

import com.tms.skv.registration_platform.domain.Sex;
import com.tms.skv.registration_platform.entity.UserEntity;
import com.tms.skv.registration_platform.mapper.UserMapper;
import com.tms.skv.registration_platform.model.UserDto;
import com.tms.skv.registration_platform.model.UserUpdateDto;
import com.tms.skv.registration_platform.service.impl.UserEntityServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.servlet.ModelAndView;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebMvcTest(value = UserController.class, excludeAutoConfiguration =  SecurityAutoConfiguration.class)
class UserControllerTest {
    @MockBean
    private UserEntityServiceImpl userEntityService;

    @MockBean
    private UserMapper userMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void save() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .param("username", "sviatlana")
                        .param("password", "sviatlana")
                        .param("address", "Torunska")
                        .param("email","sviatlana.skiba@gmail.com")
                        .param("birthday", "1994-02-22")
                        .param("firstName", "Sviatlana")
                        .param("lastName", "Kryskevich")
                        .param("sex", Sex.WOMAN.toString())
                        .param("phoneNumber", "+48787916247")
                        .contentType(new MediaType(MediaType.APPLICATION_FORM_URLENCODED, StandardCharsets.UTF_8)))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        String viewName = modelAndView.getViewName();
        Assertions.assertThat(viewName).isEqualTo("login");
    }

    @Test
    void saveWithValidErrors() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .param("username", "")
                        .param("password", "sviatlana")
                        .param("address", "Torunska")
                        .param("email","sviatlana.skiba@gmail.com")
                        .param("birthday", "1994-02-22")
                        .param("firstName", "Sviatlana")
                        .param("lastName", "Kryskevich")
                        .param("sex", Sex.WOMAN.toString())
                        .param("phoneNumber", "+48787916247")
                        .contentType(new MediaType(MediaType.APPLICATION_FORM_URLENCODED, StandardCharsets.UTF_8)))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        String viewName = modelAndView.getViewName();
        var sexes = modelAndView.getModel().get("sexes");
        Assertions.assertThat(viewName).isEqualTo("register");
        Assertions.assertThat(sexes).isInstanceOf(Sex[].class);
        Sex[] sexes1 = (Sex[]) sexes;
        Assertions.assertThat(sexes1).containsAll(Arrays.stream(Sex.values()).toList());
    }

    @Test
    void registerPage() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/register"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        String viewName = modelAndView.getViewName();
        var sexes = modelAndView.getModel().get("sexes");
        var dto = (UserDto) modelAndView.getModel().get("userDto");

        Assertions.assertThat(viewName).isEqualTo("register");
        Assertions.assertThat(sexes).isInstanceOf(Sex[].class);
        Sex[] sexes1 = (Sex[]) sexes;
        Assertions.assertThat(sexes1).containsAll(Arrays.stream(Sex.values()).toList());
        Assertions.assertThat(dto).isNotNull();
    }

    @Test
    void updateFormPage() throws Exception {
        UserEntity user = UserEntity.builder()
                .id(1)
                .login("sviatlana")
                .password("$2a$10$oArt.zTpia2sqFq1pkHPMuYJrMMfw77l3MSkPsoXO0DAoyQTZ9UyS")
                .address("Torunska")
                .email("sviatlana.skiba@gmail.com")
                .birthday(LocalDate.of(1996, 8, 8))
                .firstName("Sviatlana")
                .lastName("Kryskevich")
                .sex(Sex.WOMAN)
                .phoneNumber("+48787916247")
                .perm("ROLE_USER")
                .build();
        UserUpdateDto updateDto = UserUpdateDto.builder()
                .id(1)
                .username("sviatlana")
                .password("$2a$10$oArt.zTpia2sqFq1pkHPMuYJrMMfw77l3MSkPsoXO0DAoyQTZ9UyS")
                .address("Torunska")
                .email("sviatlana.skiba@gmail.com")
                .birthday(LocalDate.of(1996, 8, 8))
                .firstName("Sviatlana")
                .lastName("Kryskevich")
                .sex(Sex.WOMAN)
                .phoneNumber("+48787916247")
                .build();
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        UserDetails userDetails = new User("sviatlana", "password", authorities);

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
        Mockito.when(userEntityService.getByUsername(userDetails.getUsername())).thenReturn(user);
        Mockito.when(userMapper.toUpdateDto(user)).thenReturn(updateDto);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/updateUser"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        String viewName = modelAndView.getViewName();
        var sexes = modelAndView.getModel().get("sexes");
        var dto = (UserUpdateDto) modelAndView.getModel().get("userUpdateDto");

        Assertions.assertThat(viewName).isEqualTo("updateUser");
        Assertions.assertThat(sexes).isInstanceOf(Sex[].class);
        Sex[] sexes1 = (Sex[]) sexes;
        Assertions.assertThat(sexes1).containsAll(Arrays.stream(Sex.values()).toList());
        Assertions.assertThat(dto).isEqualTo(updateDto);
    }

    @Test
    void updateUser() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/updateUser")
                        .param("id", "1")
                        .param("username", "sviatlana")
                        .param("password", "sviatlana")
                        .param("address", "Torunska")
                        .param("email","sviatlana.skiba@gmail.com")
                        .param("birthday", "1994-02-22")
                        .param("firstName", "Sviatlana")
                        .param("lastName", "Kryskevich")
                        .param("sex", Sex.WOMAN.toString())
                        .param("phoneNumber", "+48787916247")
                        .contentType(new MediaType(MediaType.APPLICATION_FORM_URLENCODED, StandardCharsets.UTF_8)))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        String viewName = modelAndView.getViewName();
        String message = (String) modelAndView.getModel().get("message");
        var sexes = modelAndView.getModel().get("sexes");
        Assertions.assertThat(sexes).isInstanceOf(Sex[].class);
        Sex[] sexes1 = (Sex[]) sexes;
        Assertions.assertThat(sexes1).containsAll(Arrays.stream(Sex.values()).toList());
        Assertions.assertThat(viewName).isEqualTo("updateUser");
        Assertions.assertThat(message).isEqualTo("Редактирование сохранено");
    }

    @Test
    void updateUserWithValidErrors() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/updateUser")
                        .param("id", "1")
                        .param("username", "")
                        .param("password", "sviatlana")
                        .param("address", "Torunska")
                        .param("email","sviatlana.skiba@gmail.com")
                        .param("birthday", "1994-02-22")
                        .param("firstName", "Sviatlana")
                        .param("lastName", "Kryskevich")
                        .param("sex", Sex.WOMAN.toString())
                        .param("phoneNumber", "+48787916247")
                        .contentType(new MediaType(MediaType.APPLICATION_FORM_URLENCODED, StandardCharsets.UTF_8)))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        String viewName = modelAndView.getViewName();
        var sexes = modelAndView.getModel().get("sexes");
        Assertions.assertThat(sexes).isInstanceOf(Sex[].class);
        Sex[] sexes1 = (Sex[]) sexes;
        Assertions.assertThat(sexes1).containsAll(Arrays.stream(Sex.values()).toList());
        Assertions.assertThat(viewName).isEqualTo("updateUser");
    }
}