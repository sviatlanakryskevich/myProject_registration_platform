package com.tms.skv.registration_platform.mapper;

import com.tms.skv.registration_platform.entity.DoctorEntity;
import com.tms.skv.registration_platform.model.DoctorDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(componentModel = "spring")
public interface DoctorMapper{
    @Mapping(target = "id", source = "id")
    @Mapping(target = "fullName", source = "fullName")
    @Mapping(target = "doctorSpecialty", source = "doctorSpecialty")
    @Mapping(target = "experience", source = "experience")
    DoctorEntity toEntity(DoctorDto dto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "fullName", source = "fullName")
    @Mapping(target = "doctorSpecialty", source = "doctorSpecialty")
    @Mapping(target = "experience", source = "experience")
    DoctorDto toDto(DoctorEntity entity);
}
