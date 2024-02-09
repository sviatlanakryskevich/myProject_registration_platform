package com.tms.skv.registration_platform.service.impl;

import com.tms.skv.registration_platform.entity.OrderEntity;
import com.tms.skv.registration_platform.entity.UserEntity;
import com.tms.skv.registration_platform.exc.NotFoundException;
import com.tms.skv.registration_platform.mapper.UserMapper;
import com.tms.skv.registration_platform.model.UserDto;
import com.tms.skv.registration_platform.model.UserUpdateDto;
import com.tms.skv.registration_platform.repository.UserRepository;
import com.tms.skv.registration_platform.service.UserEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserEntityServiceImpl implements UserEntityService {
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final UserMapper mapper;
    @Transactional
    @Override
    public void save(UserDto user){
        UserEntity userEntity = UserEntity.builder()
                .address(user.getAddress())
                .email(user.getEmail())
                .login(user.getUsername())
                .sex(user.getSex())
                .birthday(user.getBirthday())
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .password(encoder.encode(user.getPassword()))
                .phoneNumber(user.getPhoneNumber())
                .perm("ROLE_USER")
                .build();
        userRepository.save(userEntity);
    }

    @Override
    @Transactional
    public void update(UserUpdateDto user) {
        Integer id = user.getId();
        Optional<UserEntity> userOpt = userRepository.findById(id);
        if(userOpt.isPresent()){
            UserEntity oldUser = userOpt.get();
            UserEntity newUser = mapper.toUpdateEntity(user);
            newUser.setLogin(oldUser.getLogin());
            newUser.setPassword(oldUser.getPassword());
            newUser.setPerm("ROLE_USER");
            userRepository.save(newUser);
        }else {
            throw new NotFoundException("Doctor with this id not found");
        }
    }


}
