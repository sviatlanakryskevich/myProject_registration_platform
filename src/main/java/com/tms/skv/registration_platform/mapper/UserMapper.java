package com.tms.skv.registration_platform.mapper;

import com.tms.skv.registration_platform.entity.DoctorEntity;
import com.tms.skv.registration_platform.entity.UserEntity;
import com.tms.skv.registration_platform.model.DoctorDto;
import com.tms.skv.registration_platform.model.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "login", source = "username")
    UserEntity toEntity(UserDto dto);


    @Mapping(target = "username", source = "login")
    UserDto toDto(UserEntity entity);
}
