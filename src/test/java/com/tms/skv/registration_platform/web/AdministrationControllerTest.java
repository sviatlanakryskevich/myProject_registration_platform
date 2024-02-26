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
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.servlet.ModelAndView;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@WebMvcTest(value = AdministrationController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class AdministrationControllerTest {

    @MockBean
    private DoctorEntityServiceImpl doctorEntityService;

    @MockBean
    private DoctorMapper doctorMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
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
    void saveDoctor() throws Exception {
        DoctorEntity doctor1 = DoctorEntity.builder()
                .doctorSpecialty(DoctorSpecialty.CARDIOLOGIST)
                .id(1)
                .experience(12.0)
                .fullName("Сойко Вера Владимировна")
                .build();
        Mockito.when(doctorEntityService.findAll()).thenReturn(List.of(doctor1));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/createDoctor")
                .param("doctorSpecialty", DoctorSpecialty.CARDIOLOGIST.toString())
                .param("experience", "12.0")
                .param("fullName", "Сойко Вера Владимировна")
                .contentType(new MediaType(MediaType.APPLICATION_FORM_URLENCODED, StandardCharsets.UTF_8)))
                .andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        String viewName = modelAndView.getViewName();

        List<DoctorEntity> doctors = (List<DoctorEntity>) modelAndView.getModel().get("allDoctors");

        Assertions.assertThat(viewName).isEqualTo("admin");
        Assertions.assertThat(doctors).hasSize(1);
        Assertions.assertThat(doctors).contains(doctor1);
    }

    @Test
    void updateDoctor() throws Exception {
        DoctorEntity doctor1 = DoctorEntity.builder()
                .doctorSpecialty(DoctorSpecialty.CARDIOLOGIST)
                .id(1)
                .experience(12.0)
                .fullName("Сойко Вера Владимировна")
                .build();
        Mockito.when(doctorEntityService.findAll()).thenReturn(List.of(doctor1));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/createDoctor")
                        .param("id", "1")
                        .param("doctorSpecialty", DoctorSpecialty.CARDIOLOGIST.toString())
                        .param("experience", "12.0")
                        .param("fullName", "Сойко Вера Владимировна")
                        .contentType(new MediaType(MediaType.APPLICATION_FORM_URLENCODED, StandardCharsets.UTF_8)))
                .andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        String viewName = modelAndView.getViewName();

        List<DoctorEntity> doctors = (List<DoctorEntity>) modelAndView.getModel().get("allDoctors");

        Assertions.assertThat(viewName).isEqualTo("admin");
        Assertions.assertThat(doctors).hasSize(1);
        Assertions.assertThat(doctors).contains(doctor1);
    }

    @Test
    void saveDoctorWithErrorsFields() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/createDoctor")
                        .param("doctorSpecialty", DoctorSpecialty.CARDIOLOGIST.toString())
                        .param("experience", "12.0")
                        .contentType(new MediaType(MediaType.APPLICATION_FORM_URLENCODED, StandardCharsets.UTF_8)))
                .andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        String viewName = modelAndView.getViewName();
        var specialties = modelAndView.getModel().get("specialties");
        Assertions.assertThat(specialties).isInstanceOf(DoctorSpecialty[].class);
        DoctorSpecialty[] doctorSpecialties = (DoctorSpecialty[]) specialties;
        Assertions.assertThat(doctorSpecialties).containsAll(Arrays.stream(DoctorSpecialty.values()).toList());
        Assertions.assertThat(viewName).isEqualTo("createDoctor");
    }

    @Test
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