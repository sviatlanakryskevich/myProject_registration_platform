package com.tms.skv.registration_platform.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tms.skv.registration_platform.domain.DoctorSpecialty;
import com.tms.skv.registration_platform.entity.DoctorEntity;
import com.tms.skv.registration_platform.mapper.DoctorMapper;
import com.tms.skv.registration_platform.model.DoctorDto;
import com.tms.skv.registration_platform.service.impl.DoctorEntityServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.servlet.ModelAndView;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(AdministrationController.class)
class AdministrationControllerTest {

    @MockBean
    private DoctorEntityServiceImpl doctorEntityService;

    @MockBean
    private DoctorMapper doctorMapper;

    @Autowired
    private ObjectMapper objectMapper;

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

        Mockito.when(doctorEntityService.findAll()).thenReturn(List.of(doctor1, doctor2));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/admin"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        String viewName = modelAndView.getViewName();
        var allDoctors = (List<DoctorEntity>) modelAndView.getModel().get("allDoctors");

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
        var dto = (DoctorDto) modelAndView.getModel().get("doctorDto");

        Assertions.assertThat(viewName).isEqualTo("createDoctor");
        Assertions.assertThat(specialties).isInstanceOf(DoctorSpecialty[].class);
        DoctorSpecialty[] doctorSpecialties = (DoctorSpecialty[]) specialties;
        Assertions.assertThat(doctorSpecialties).containsAll(Arrays.stream(DoctorSpecialty.values()).toList());
        Assertions.assertThat(dto).isNotNull();
    }

    @Test
    @WithMockUser("qwerty")
    void updateFormPage() throws Exception {
        DoctorEntity doctor = DoctorEntity.builder()
                .doctorSpecialty(DoctorSpecialty.CARDIOLOGIST)
                .id(1)
                .experience(2.0)
                .fullName("Сойко Вера Владимировна")
                .build();
        DoctorDto doctorDto = DoctorDto.builder()
                .doctorSpecialty(DoctorSpecialty.CARDIOLOGIST)
                .id(1)
                .experience(2.0)
                .fullName("Сойко Вера Владимировна")
                .build();
        Mockito.when(doctorEntityService.findById(1)).thenReturn(doctor);
        Mockito.when(doctorMapper.toDto(doctor)).thenReturn(doctorDto);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/updateDoctor")
                        .param("doctorId", "1"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        String viewName = modelAndView.getViewName();
        var specialties = modelAndView.getModel().get("specialties");
        var dto = (DoctorDto) modelAndView.getModel().get("doctorDto");

        Assertions.assertThat(viewName).isEqualTo("createDoctor");
        Assertions.assertThat(specialties).isInstanceOf(DoctorSpecialty[].class);
        DoctorSpecialty[] doctorSpecialties = (DoctorSpecialty[]) specialties;
        Assertions.assertThat(doctorSpecialties).containsAll(Arrays.stream(DoctorSpecialty.values()).toList());
        Assertions.assertThat(dto).isEqualTo(doctorDto);
    }

    @Test
    @WithMockUser(username = "qwerty", roles = "ADMIN")
    void saveDoctor() throws Exception {
        DoctorDto doctorDto = DoctorDto.builder()
                .id(null)
                .doctorSpecialty(DoctorSpecialty.CARDIOLOGIST)
                .experience(12.0)
                .fullName("")
                .build();
        String json = objectMapper.writeValueAsString(doctorDto);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/createDoctor")
                        .content(json).contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8)))
                .andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        String viewName = modelAndView.getViewName();
        var specialties = modelAndView.getModel().get("specialties");

        Assertions.assertThat(viewName).isEqualTo("admin");

        /*DoctorEntity doctor1 = DoctorEntity.builder()
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
        String json = objectMapper.writeValueAsString(doctorDto);
        Mockito.when(doctorEntityService.findAll()).thenReturn(List.of(doctor1, doctor2));

        // Perform the POST request and assert the response
        mockMvc.perform(MockMvcRequestBuilders.post("/createDoctor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("admin"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("allDoctors"));*/

       /* Assertions.assertThat(allDoctors).hasSize(2);*/
    }

    /*@Test
    @WithMockUser("qwerty")
    void saveDoctorWithErrorsFields() throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/createDoctor")
                        .content(json).contentType(MediaType.APPLICATION_JSON).)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(model().attributeHasErrors("person"))
                .andReturn();
    }*/

    @Test
    @WithMockUser("qwerty")
    void delete() throws Exception {
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
        DoctorEntity doctor3 = DoctorEntity.builder()
                .doctorSpecialty(DoctorSpecialty.NEUROLOGIST)
                .id(3)
                .experience(15.0)
                .fullName("Иванов Иван Иванович")
                .build();

        Mockito.when(doctorEntityService.findAll()).thenReturn(List.of(doctor1, doctor3));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/deleteDoctor")
                        .param("doctorId", "2"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        String viewName = modelAndView.getViewName();
        Assertions.assertThat(viewName).isNotNull();
        var allDoctors = (List<DoctorEntity>) modelAndView.getModel().get("allDoctors");

        Assertions.assertThat(viewName).isEqualTo("admin");
        Assertions.assertThat(allDoctors).hasSize(2);
    }
}