package com.tms.skv.registration_platform.web;

import com.tms.skv.registration_platform.domain.DoctorSpecialty;
import com.tms.skv.registration_platform.domain.Record;
import com.tms.skv.registration_platform.domain.Sex;
import com.tms.skv.registration_platform.entity.DoctorEntity;
import com.tms.skv.registration_platform.entity.OrderEntity;
import com.tms.skv.registration_platform.entity.UserEntity;
import com.tms.skv.registration_platform.service.impl.DoctorEntityServiceImpl;
import com.tms.skv.registration_platform.service.impl.OrderEntityServiceImpl;
import com.tms.skv.registration_platform.service.impl.UserEntityServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@WebMvcTest(value = OrderController.class, excludeAutoConfiguration =  SecurityAutoConfiguration.class)
class OrderControllerTest {
    @MockBean
    private DoctorEntityServiceImpl doctorEntityService;

    @MockBean
    private OrderEntityServiceImpl orderEntityService;

    @MockBean
    private UserEntityServiceImpl userEntityService;

    @Autowired
    private MockMvc mockMvc;

    @Test
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

    @Test
    void getSchedule() throws Exception {
        LocalDateTime dateTime = LocalDateTime.of(2024, 2, 22, 8, 30);
        LocalDate mon = LocalDate.of(2024, 2, 19);
        LocalDate tues = LocalDate.of(2024, 2, 20);
        LocalDate wed = LocalDate.of(2024, 2, 21);
        LocalDate thurs = LocalDate.of(2024, 2, 22);
        LocalDate fri = LocalDate.of(2024, 2, 23);
        List<LocalDate> week = List.of(mon, tues, wed, thurs, fri);
        DoctorEntity doctor1 = DoctorEntity.builder()
                .doctorSpecialty(DoctorSpecialty.CARDIOLOGIST)
                .id(1)
                .experience(2.0)
                .fullName("Сойко Вера Владимировна")
                .build();
        LocalTime time1 = LocalTime.of(8, 0);
        LocalTime time2 = LocalTime.of(8, 30);

        Record record = new Record();
        List<Record> records = List.of(record);
        Map<LocalTime, List<Record>> intervals = new HashMap<>();
        intervals.put(time1, records);
        intervals.put(time2, records);

        Mockito.when(doctorEntityService.findById(1)).thenReturn(doctor1);
        Mockito.when(orderEntityService.getCurrentWeek(dateTime)).thenReturn(week);
        Mockito.when(orderEntityService.getTimeSlotsMap(1, List.of(mon, tues, wed, thurs, fri))).thenReturn(intervals);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/schedule/{id}", doctor1.getId())
                        .param("dateTime", "2024-02-22T08:30:00"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        String viewName = modelAndView.getViewName();
        var currentWeek = (List<LocalDate>)modelAndView.getModel().get("currentWeek");
        var intervals1 = modelAndView.getModel().get("intervals");
        var doctor = modelAndView.getModel().get("doctor");
        var next1 = modelAndView.getModel().get("next");
        var previous1 = modelAndView.getModel().get("previous");

        Assertions.assertThat(viewName).isEqualTo("schedule");
        Assertions.assertThat(currentWeek).hasSize(5);
        Assertions.assertThat(currentWeek).contains(mon, thurs, wed, tues, fri);
        Assertions.assertThat(intervals1).isEqualTo(intervals);
        Assertions.assertThat(next1).isInstanceOf(LocalDateTime.class);
        Assertions.assertThat(previous1).isInstanceOf(LocalDateTime.class);
        Assertions.assertThat(doctor).isEqualTo(doctor1);
    }

    @Test
    void createOrder() throws Exception {
        DoctorEntity doctor1 = DoctorEntity.builder()
                .doctorSpecialty(DoctorSpecialty.CARDIOLOGIST)
                .id(1)
                .experience(2.0)
                .fullName("Сойко Вера Владимировна")
                .build();
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
        LocalDateTime appointment = LocalDateTime.of(2024, 2, 22,8, 30);
        OrderEntity order = OrderEntity.builder()
                .doctor(doctor1)
                .appointmentTime(appointment)
                .id(1)
                .user(user)
                .build();
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        UserDetails userDetails = new User("sviatlana", "password", authorities);

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
        Mockito.when(doctorEntityService.findById(1)).thenReturn(doctor1);
        Mockito.when(userEntityService.getByUsername(userDetails.getUsername())).thenReturn(user);
        Mockito.when(orderEntityService.createOrder(doctor1, user,appointment)).thenReturn(order);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/createOrder")
                        .param("doctorId", "1")
                        .param("appointment", "2024-02-22T08:30:00"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        String viewName = modelAndView.getViewName();
        var doctorId = modelAndView.getModel().get("doctorId");
        var now = modelAndView.getModel().get("now");
        var savedOrder = modelAndView.getModel().get("savedOrder");
        Assertions.assertThat(viewName).isEqualTo("order");
        Assertions.assertThat(doctorId).isEqualTo(doctor1.getId());
        Assertions.assertThat(now).isInstanceOf(LocalDateTime.class);
        Assertions.assertThat(savedOrder).isEqualTo(order);
    }

    @Test
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
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        UserDetails userDetails = new User("sviatlana", "password", authorities);

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
        Mockito.when(userEntityService.getByUsername(userDetails.getUsername())).thenReturn(user);
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
        OrderEntity order1 = OrderEntity.builder()
                .doctor(doctor1)
                .appointmentTime(LocalDateTime.of(2024, 2, 21, 11, 0))
                .id(1)
                .user(user)
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