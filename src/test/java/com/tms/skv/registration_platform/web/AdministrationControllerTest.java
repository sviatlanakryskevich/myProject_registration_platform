package com.tms.skv.registration_platform.web;

import com.tms.skv.registration_platform.domain.DoctorSpecialty;
import com.tms.skv.registration_platform.entity.DoctorEntity;
import com.tms.skv.registration_platform.mapper.DoctorMapper;
import com.tms.skv.registration_platform.model.DoctorDto;
import com.tms.skv.registration_platform.service.impl.DoctorEntityServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

@WebMvcTest(AdministrationController.class)
class AdministrationControllerTest {

    @MockBean
    private DoctorEntityServiceImpl doctorEntityService;

    @MockBean
    private  DoctorMapper doctorMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser("qwerty")
    void testGetAllDoctors() throws Exception {
        DoctorEntity doctor1 = DoctorEntity.builder()
                .doctorSpecialty(DoctorSpecialty.CARDIOLOGIST)
                .id(1)
                .experience(2.0)
                .fullName("Сойко Вера Владимировна")
                .build();
        DoctorEntity doctor2 = DoctorEntity.builder()
                .doctorSpecialty(DoctorSpecialty.ENDOCRINOLOGIST)
                .id(2)
                .experience(12.0)
                .fullName("Дуб Вера Владимировна")
                .build();

        Mockito.when(doctorEntityService.findAll()).thenReturn(List.of(doctor1,doctor2));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/admin"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        String viewName = modelAndView.getViewName();
        var allDoctors = (List<DoctorEntity>)modelAndView.getModel().get("allDoctors");

        Assertions.assertThat(viewName).isEqualTo("admin");
        Assertions.assertThat(allDoctors).hasSize(2);
    }

    @Test
    @WithMockUser("qwerty")
    void createFormPage() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/createDoctor"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        String viewName = modelAndView.getViewName();
        var specialties = modelAndView.getModel().get("specialties");
        var dto = (DoctorDto)modelAndView.getModel().get("doctorDto");

        Assertions.assertThat(viewName).isEqualTo("createDoctor");
        Assertions.assertThat(specialties).isInstanceOf(DoctorSpecialty[].class);
        DoctorSpecialty[] doctorSpecialties = (DoctorSpecialty[]) specialties;
        Assertions.assertThat(doctorSpecialties).containsAll(Arrays.stream(DoctorSpecialty.values()).toList());
        Assertions.assertThat(dto).isNotNull();
    }

    @Test
    @WithMockUser("qwerty")
    void updateFormPage() {
    }

    @Test
    @WithMockUser("qwerty")
    void saveDoctor() {
    }

    @Test
    @WithMockUser("qwerty")
    void delete() {
    }
}