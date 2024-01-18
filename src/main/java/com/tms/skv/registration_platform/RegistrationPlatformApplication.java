package com.tms.skv.registration_platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class RegistrationPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(RegistrationPlatformApplication.class, args);
    }

}
