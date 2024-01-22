package com.tms.skv.registration_platform.service;

import com.tms.skv.registration_platform.domain.DoctorSpecialty;
import com.tms.skv.registration_platform.entity.DoctorEntity;
import com.tms.skv.registration_platform.model.DoctorDto;

import java.util.List;

public interface DoctorEntityService {
    List<DoctorEntity> findAll();
    List<DoctorEntity> findBySpecialty(DoctorSpecialty specialty);
    void save(DoctorDto doctorDto);
    void update(Integer id);
    void delete(Integer id);
}
