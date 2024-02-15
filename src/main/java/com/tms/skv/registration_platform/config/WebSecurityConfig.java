package com.tms.skv.registration_platform.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;
@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(cust -> {
            cust.requestMatchers("/register", "/login", "/error", "/css/**").permitAll()
                    .requestMatchers("/main", "/get/**", "/schedule/**", "/createOrder",
                            "/logout", "/updateUser", "/getOrders", "/deleteOrder").authenticated()
                    .requestMatchers("/admin", "/createDoctor", "/deleteDoctor", "/updateDoctor").hasRole("ADMIN");
        });
        http.formLogin(cust -> {
            cust.loginPage("/login");
            cust.loginProcessingUrl("/login");
            cust.usernameParameter("username");
            cust.passwordParameter("password");
            cust.successHandler(((request, response, authentication) -> {
                var authorities = authentication.getAuthorities();
                List<String> stringAuthorities = authorities.stream().map(GrantedAuthority::getAuthority).toList();
                if (stringAuthorities.contains("ROLE_ADMIN")) {
                    response.sendRedirect("/admin");
                } else {
                    response.sendRedirect("/main");
                }
            }));
            cust.failureHandler(customAuthenticationFailureHandler);
        });
        http.logout(cust -> {
            cust.logoutUrl("/logout");
            cust.logoutSuccessUrl("/login");
            cust.invalidateHttpSession(true);

        });
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(AbstractHttpConfigurer::disable);
        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
