package com.tms.skv.registration_platform.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tms.skv.registration_platform.domain.DoctorSpecialty;
import com.tms.skv.registration_platform.domain.Sex;
import com.tms.skv.registration_platform.entity.DoctorEntity;
import com.tms.skv.registration_platform.entity.OrderEntity;
import com.tms.skv.registration_platform.entity.UserEntity;
import com.tms.skv.registration_platform.service.UserEntityService;
import com.tms.skv.registration_platform.service.impl.DoctorEntityServiceImpl;
import com.tms.skv.registration_platform.service.impl.OrderEntityServiceImpl;
import com.tms.skv.registration_platform.service.impl.UserEntityServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(OrderController.class)
class OrderControllerTest {
    @MockBean
    private DoctorEntityServiceImpl doctorEntityService;

    @MockBean
    private OrderEntityServiceImpl orderEntityService;

    @MockBean
    private UserEntityServiceImpl userEntityService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser("sviatlana")
    void mainPage() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/main"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        String viewName = modelAndView.getViewName();
        var specialties = modelAndView.getModel().get("specialties");

        Assertions.assertThat(viewName).isEqualTo("main");
        Assertions.assertThat(specialties).isInstanceOf(DoctorSpecialty[].class);
        DoctorSpecialty[] doctorSpecialties = (DoctorSpecialty[]) specialties;
        Assertions.assertThat(doctorSpecialties).containsAll(Arrays.stream(DoctorSpecialty.values()).toList());
    }

    @Test
    @WithMockUser("sviatlana")
    void getDoctorsBySpecialty() throws Exception {
        DoctorSpecialty cardiologist = DoctorSpecialty.CARDIOLOGIST;
        DoctorEntity doctor1 = DoctorEntity.builder()
                .doctorSpecialty(DoctorSpecialty.CARDIOLOGIST)
                .id(1)
                .experience(2.0)
                .fullName("Сойко Вера Владимировна")
                .build();
        Mockito.when(doctorEntityService.findBySpecialty(cardiologist)).thenReturn(List.of(doctor1));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/get/{specialty}", cardiologist))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        String viewName = modelAndView.getViewName();
        var doctorsBySpecialty = (List<DoctorEntity>) modelAndView.getModel().get("doctorsBySpecialty");
        Object now = modelAndView.getModel().get("now");

        Assertions.assertThat(viewName).isEqualTo("specialty");
        Assertions.assertThat(now).isInstanceOf(LocalDateTime.class);
        Assertions.assertThat(doctorsBySpecialty).hasSize(1);
        Assertions.assertThat(doctorsBySpecialty).contains(doctor1);
    }

    /*@Test
    void getSchedule() throws Exception {
        LocalDateTime dateTime = LocalDateTime.of(2024, 2, 22, 8, 30);
        LocalDate mon = LocalDate.of(2024, 2, 19);
        LocalDate tues = LocalDate.of(2024, 2, 20);
        LocalDate wed = LocalDate.of(2024, 2, 21);
        LocalDate thurs = LocalDate.of(2024, 2, 22);
        LocalDate fri = LocalDate.of(2024, 2, 23);
        DoctorEntity doctor1 = DoctorEntity.builder()
                .doctorSpecialty(DoctorSpecialty.CARDIOLOGIST)
                .id(1)
                .experience(2.0)
                .fullName("Сойко Вера Владимировна")
                .build();
        LocalTime time1 = LocalTime.of(8, 0);
        LocalTime time2 = LocalTime.of(8, 30);
        Mockito.when(doctorEntityService.findById(1)).thenReturn(doctor1);
        Mockito.when(orderEntityService.getCurrentWeek(dateTime)).thenReturn(List.of(mon, tues, wed, thurs, fri));
        Mockito.when(orderEntityService.getTimeSlotsMap(1, List.of(mon, tues, wed, thurs, fri))).thenReturn()
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/schedule/{id}", doctor1.getId())
                        .param("dateTime", "2024-02-22T08:30:00"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        String viewName = modelAndView.getViewName();
        var doctorsBySpecialty = (List<DoctorEntity>) modelAndView.getModel().get("doctorsBySpecialty");
        Object now = modelAndView.getModel().get("now");
    }*/

   /* @Test
    void createOrder() throws Exception {
        DoctorEntity doctor1 = DoctorEntity.builder()
                .doctorSpecialty(DoctorSpecialty.CARDIOLOGIST)
                .id(1)
                .experience(2.0)
                .fullName("Сойко Вера Владимировна")
                .build();
        Mockito.when(doctorEntityService.findById(1)).thenReturn(doctor1);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/createOrder")
                        .param("doctorId", "1")
                        .param("appointment", "2024-02-22T08:30:00").principal())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }*/

    @Test
    @WithMockUser("sviatlana")
    void getOrders() throws Exception {
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
        DoctorEntity doctor1 = DoctorEntity.builder()
                .doctorSpecialty(DoctorSpecialty.CARDIOLOGIST)
                .id(1)
                .experience(2.0)
                .fullName("Сойко Вера Владимировна")
                .build();
        DoctorEntity doctor2 = DoctorEntity.builder()
                .doctorSpecialty(DoctorSpecialty.NEUROLOGIST)
                .id(2)
                .experience(12.0)
                .fullName("Сойко Иван Владимировна")
                .build();
        OrderEntity order1 = OrderEntity.builder()
                .doctor(doctor1)
                .appointmentTime(LocalDateTime.of(2024, 2, 21, 11, 0))
                .id(1)
                .user(user)
                .build();
        OrderEntity order2 = OrderEntity.builder()
                .doctor(doctor2)
                .appointmentTime(LocalDateTime.of(2024, 2, 22, 9, 30))
                .id(2)
                .user(user)
                .build();

        Mockito.when(userEntityService.getByUsername("sviatlana")).thenReturn(user);
        Mockito.when(orderEntityService.getOrdersByUser(1)).thenReturn(List.of(order1, order2));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/getOrders"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        String viewName = modelAndView.getViewName();
        var orders = (List<OrderEntity>)modelAndView.getModel().get("orders");
        var today = (LocalDateTime) modelAndView.getModel().get("today");

        Assertions.assertThat(viewName).isEqualTo("myOrders");
        Assertions.assertThat(orders).hasSize(2);
        Assertions.assertThat(orders).contains(order1, order2);
        Assertions.assertThat(today).isInstanceOf(LocalDateTime.class);
    }

    @Test
    @WithMockUser("sviatlana")
    void deleteOrder() throws Exception {
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
        DoctorEntity doctor1 = DoctorEntity.builder()
                .doctorSpecialty(DoctorSpecialty.CARDIOLOGIST)
                .id(1)
                .experience(2.0)
                .fullName("Сойко Вера Владимировна")
                .build();
        DoctorEntity doctor2 = DoctorEntity.builder()
                .doctorSpecialty(DoctorSpecialty.NEUROLOGIST)
                .id(2)
                .experience(12.0)
                .fullName("Сойко Иван Владимировна")
                .build();
        OrderEntity order1 = OrderEntity.builder()
                .doctor(doctor1)
                .appointmentTime(LocalDateTime.of(2024, 2, 21, 11, 0))
                .id(1)
                .user(user)
                .build();

        Mockito.when(userEntityService.getByUsername("sviatlana")).thenReturn(user);
        Mockito.when(orderEntityService.getOrdersByUser(1)).thenReturn(List.of(order1));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/deleteOrder").param("id", "2"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        String viewName = modelAndView.getViewName();
        var orders = (List<OrderEntity>)modelAndView.getModel().get("orders");
        var today = (LocalDateTime) modelAndView.getModel().get("today");

        Assertions.assertThat(viewName).isEqualTo("myOrders");
        Assertions.assertThat(orders).hasSize(1);
        Assertions.assertThat(orders).contains(order1);
        Assertions.assertThat(today).isInstanceOf(LocalDateTime.class);
    }
}