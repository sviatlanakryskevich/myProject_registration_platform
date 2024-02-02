package com.tms.skv.registration_platform.service;

import com.tms.skv.registration_platform.domain.DoctorSpecialty;
import com.tms.skv.registration_platform.entity.DoctorEntity;
import com.tms.skv.registration_platform.model.DoctorDto;

import java.util.List;
import java.util.Optional;

public interface DoctorEntityService {
    List<DoctorEntity> findAll();
    DoctorEntity findById(Integer id);
    List<DoctorEntity> findBySpecialty(DoctorSpecialty specialty);
    void save(DoctorDto doctorDto);
    void update(DoctorDto dto);
    void delete(Integer id);
}
