package com.tms.skv.registration_platform.service.impl;

import com.tms.skv.registration_platform.domain.Sex;
import com.tms.skv.registration_platform.entity.UserEntity;
import com.tms.skv.registration_platform.exc.NotFoundException;
import com.tms.skv.registration_platform.exc.NotUniqueUserNameException;
import com.tms.skv.registration_platform.mapper.UserMapper;
import com.tms.skv.registration_platform.model.UserDto;
import com.tms.skv.registration_platform.model.UserUpdateDto;
import com.tms.skv.registration_platform.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserEntityServiceImplTest {
    @Mock
    private PasswordEncoder encoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper mapper;
    @InjectMocks
    private UserEntityServiceImpl userEntityService;

    @Test
    void testGetById() {
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
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        UserEntity userEntity = userEntityService.getById(1);
        Assertions.assertThat(userEntity).isEqualTo(user);
    }

    @Test
    void testGetByIdThrowException() {
        when(userRepository.findById(2)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> userEntityService.getById(2)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void testSave() {
        UserDto userDto = UserDto.builder()
                .username("sviatlana")
                .password("sviatlana")
                .address("Torunska")
                .email("sviatlana.skiba@gmail.com")
                .birthday(LocalDate.of(1996, 8, 8))
                .firstName("Sviatlana")
                .lastName("Kryskevich")
                .sex(Sex.WOMAN)
                .phoneNumber("+48787916247")
                .build();
        UserEntity user = UserEntity.builder()
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
        when(encoder.encode("sviatlana")).thenReturn("$2a$10$oArt.zTpia2sqFq1pkHPMuYJrMMfw77l3MSkPsoXO0DAoyQTZ9UyS");
        userEntityService.save(userDto);

        Mockito.verify(userRepository).save(user);
    }

    @Test
    void testSaveThrowException() {
        UserDto userDto = UserDto.builder()
                .username("sviatlana")
                .password("sviatlana")
                .address("Torunska")
                .email("sviatlana.skiba@gmail.com")
                .birthday(LocalDate.of(1996, 8, 8))
                .firstName("Sviatlana")
                .lastName("Kryskevich")
                .sex(Sex.WOMAN)
                .phoneNumber("+48787916247")
                .build();
        UserEntity user = UserEntity.builder()
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
        when(encoder.encode("sviatlana")).thenReturn("$2a$10$oArt.zTpia2sqFq1pkHPMuYJrMMfw77l3MSkPsoXO0DAoyQTZ9UyS");
        when(userRepository.save(user)).thenThrow(DataIntegrityViolationException.class);
        Assertions.assertThatThrownBy(() -> userEntityService.save(userDto)).isInstanceOf(NotUniqueUserNameException.class);
    }

    @Test
    void testUpdate() {
        UserUpdateDto updateDto = UserUpdateDto.builder()
                .id(1)
                .address("Torunska")
                .email("sviatlana.skiba@gmail.com")
                .birthday(LocalDate.of(1996, 8, 8))
                .firstName("Sviatlana")
                .lastName("Skiba")
                .sex(Sex.WOMAN)
                .phoneNumber("+48787916246")
                .build();

        UserEntity oldUserEntity = UserEntity.builder()
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

        UserEntity newUserEntity = UserEntity.builder()
                .id(1)
                .login("sviatlana")
                .password("$2a$10$oArt.zTpia2sqFq1pkHPMuYJrMMfw77l3MSkPsoXO0DAoyQTZ9UyS")
                .address("Torunska")
                .email("sviatlana.skiba@gmail.com")
                .birthday(LocalDate.of(1996, 8, 8))
                .firstName("Sviatlana")
                .lastName("Skiba")
                .sex(Sex.WOMAN)
                .phoneNumber("+48787916246")
                .perm("ROLE_USER")
                .build();
        when(userRepository.findById(1)).thenReturn(Optional.of(oldUserEntity));
        when(mapper.toUpdateEntity(updateDto)).thenReturn(newUserEntity);
        userEntityService.update(updateDto);
        Mockito.verify(userRepository).save(newUserEntity);
    }

    @Test
    void testUpdateThrowException() {
        UserUpdateDto updateDto = UserUpdateDto.builder()
                .id(1)
                .address("Torunska")
                .email("sviatlana.skiba@gmail.com")
                .birthday(LocalDate.of(1996, 8, 8))
                .firstName("Sviatlana")
                .lastName("Skiba")
                .sex(Sex.WOMAN)
                .phoneNumber("+48787916246")
                .build();
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> userEntityService.update(updateDto)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void testGetUserEntityByUsername(){
        String username = new String("sviatlana");
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
        when(userRepository.findByLogin(username)).thenReturn(Optional.of(user));
        UserEntity userEntity = userEntityService.getByUsername(username);
        Assertions.assertThat(userEntity).isEqualTo(user);
    }

    @Test
    void testGetUserEntityByUsernameThrowException(){
        String username = new String("test");
        when(userRepository.findByLogin(username)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> userEntityService.getByUsername(username)).isInstanceOf(NotFoundException.class);
    }
}