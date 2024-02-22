package com.tms.skv.registration_platform.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tms.skv.registration_platform.config.WebSecurityConfig;
import com.tms.skv.registration_platform.domain.DoctorSpecialty;
import com.tms.skv.registration_platform.domain.Sex;
import com.tms.skv.registration_platform.entity.UserEntity;
import com.tms.skv.registration_platform.mapper.DoctorMapper;
import com.tms.skv.registration_platform.mapper.UserMapper;
import com.tms.skv.registration_platform.model.DoctorDto;
import com.tms.skv.registration_platform.model.UserDto;
import com.tms.skv.registration_platform.model.UserUpdateDto;
import com.tms.skv.registration_platform.service.impl.DoctorEntityServiceImpl;
import com.tms.skv.registration_platform.service.impl.UserDetailsServiceImpl;
import com.tms.skv.registration_platform.service.impl.UserEntityServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {
    @MockBean
    private UserEntityServiceImpl userEntityService;

    @MockBean
    private UserMapper userMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void save() {
    }

    @Test
    @WithMockUser("sviatlana")
    void registerPage() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/register").secure(false))
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
    @WithMockUser(value = "sviatlana", roles = "USER")
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

        Mockito.when(userEntityService.getByUsername("sviatlana")).thenReturn(user);
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
    @WithMockUser("sviatlana")
    void updateUser() {
    }
}