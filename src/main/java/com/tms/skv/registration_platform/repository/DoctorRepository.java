package com.tms.skv.registration_platform.repository;

import com.tms.skv.registration_platform.domain.DoctorSpecialty;
import com.tms.skv.registration_platform.entity.DoctorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorRepository extends JpaRepository<DoctorEntity, Integer> {
    List<DoctorEntity> findByDoctorSpecialty(DoctorSpecialty specialty);


}
