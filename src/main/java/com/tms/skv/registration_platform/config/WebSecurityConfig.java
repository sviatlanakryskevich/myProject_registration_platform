package com.tms.skv.registration_platform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(cust -> {
            cust.requestMatchers("public","/register", "/login", "/logout", "/welcome").permitAll()
                    .requestMatchers("/main").authenticated();
                    /*.requestMatchers("/access").hasAuthority("access");*/
        });
        http.formLogin(cust -> {
            cust.loginPage("/public");
            cust.loginProcessingUrl("/login");
            cust.usernameParameter("login");
            cust.passwordParameter("cred");
            cust.successHandler(((request, response, authentication) -> {
                response.sendRedirect("/main");
            }));
            cust.failureHandler(((request, response, exception) -> {
                response.sendRedirect("/public");
            }));
        });
        http.logout(cust -> {
            cust.logoutUrl("/logout");
            cust.logoutSuccessUrl("/welcome");
            cust.invalidateHttpSession(true);

        });
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(AbstractHttpConfigurer::disable);
        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
