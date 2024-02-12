package com.tms.skv.registration_platform.service.impl;

import com.tms.skv.registration_platform.entity.UserEntity;
import com.tms.skv.registration_platform.exc.NotFoundException;
import com.tms.skv.registration_platform.exc.NotUniqueUserNameException;
import com.tms.skv.registration_platform.mapper.UserMapper;
import com.tms.skv.registration_platform.model.UserDto;
import com.tms.skv.registration_platform.model.UserUpdateDto;
import com.tms.skv.registration_platform.repository.UserRepository;
import com.tms.skv.registration_platform.service.UserEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserEntityServiceImpl implements UserEntityService {
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final UserMapper mapper;

    @Override
    public UserEntity getById(Integer id) {
        Optional<UserEntity> byId = userRepository.findById(id);
        if(byId.isPresent()){
            UserEntity user = byId.get();
            return user;
        } else {
            throw new NotFoundException("User with this id not found");
        }
    }

    @Transactional
    @Override
    public void save(UserDto user) {
        try {
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
        } catch (DataIntegrityViolationException exc) {
            throw new NotUniqueUserNameException("Пользователь с таким логином уже существует ", user);
        }
    }

    @Override
    @Transactional
    public void update(UserUpdateDto user) {
        Integer id = user.getId();
        Optional<UserEntity> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            UserEntity oldUser = userOpt.get();
            UserEntity newUser = mapper.toUpdateEntity(user);
            newUser.setLogin(oldUser.getLogin());
            newUser.setPassword(oldUser.getPassword());
            newUser.setPerm("ROLE_USER");
            userRepository.save(newUser);
        } else {
            throw new NotFoundException("User with this id not found");
        }
    }


}
