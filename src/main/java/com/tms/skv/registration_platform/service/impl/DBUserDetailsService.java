package com.tms.skv.registration_platform.service.impl;

import com.tms.skv.registration_platform.domain.Sex;
import com.tms.skv.registration_platform.entity.UserEntity;
import com.tms.skv.registration_platform.model.UserDto;
import com.tms.skv.registration_platform.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DBUserDetailsService implements UserDetailsService {
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLogin(username)
                .orElse(null);
    }
    @Transactional
    public void save(UserDto user){
        String username = user.getUsername();
        String password = user.getPassword();
        LocalDate birthday = user.getBirthday();
        String address = user.getAddress();
        String email = user.getEmail();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        Sex sex = user.getSex();
        String phoneNumber = user.getPhoneNumber();
        UserEntity userEntity = UserEntity.builder()
                .address(address)
                .email(email)
                .login(username)
                .sex(sex)
                .birthday(birthday)
                .lastName(lastName)
                .firstName(firstName)
                .password(encoder.encode(password))
                .phoneNumber(phoneNumber)
                .perm("ROLE_USER")
                .build();
        userRepository.save(userEntity);
    }
}
