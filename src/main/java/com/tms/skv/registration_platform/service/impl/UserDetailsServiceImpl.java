package com.tms.skv.registration_platform.service.impl;

import com.tms.skv.registration_platform.exc.NotFoundException;
import com.tms.skv.registration_platform.mapper.UserMapper;
import com.tms.skv.registration_platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLogin(username)
                .orElseThrow(() -> new NotFoundException("Пользователь c логином " +username+ " не найден"));
               /* .orElseThrow(() -> new UsernameNotFoundException("Пользователь c логином" +username+ " не найден"));*/
    }

}
