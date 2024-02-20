package com.tms.skv.registration_platform.mapper;

import com.tms.skv.registration_platform.entity.UserEntity;
import com.tms.skv.registration_platform.model.UserDto;
import com.tms.skv.registration_platform.model.UserUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "login", source = "username")
    UserEntity toEntity(UserDto dto);


    @Mapping(target = "username", source = "login")
    UserDto toDto(UserEntity entity);

    @Mapping(target = "login", source = "username")
    UserEntity toUpdateEntity(UserUpdateDto dto);


    @Mapping(target = "username", source = "login")
    UserUpdateDto toUpdateDto(UserEntity entity);
}
