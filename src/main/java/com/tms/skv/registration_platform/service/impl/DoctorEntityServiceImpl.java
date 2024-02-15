package com.tms.skv.registration_platform.service.impl;

import com.tms.skv.registration_platform.domain.DoctorSpecialty;
import com.tms.skv.registration_platform.entity.DoctorEntity;
import com.tms.skv.registration_platform.exc.NotFoundException;
import com.tms.skv.registration_platform.mapper.DoctorMapper;
import com.tms.skv.registration_platform.model.DoctorDto;
import com.tms.skv.registration_platform.repository.DoctorRepository;
import com.tms.skv.registration_platform.service.DoctorEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class DoctorEntityServiceImpl implements DoctorEntityService {
    private final DoctorRepository doctorRepository;
    private final DoctorMapper mapper;
    @Override
    public List<DoctorEntity> findAll() {
        return doctorRepository.findAll();
    }

    @Override
    public DoctorEntity findById(Integer id) {
        Optional<DoctorEntity> doctorOpt = doctorRepository.findById(id);
        if(doctorOpt.isPresent()){
            return doctorOpt.get();
        }else {
            throw new NotFoundException("Doctor with this id not found");
        }
    }

    @Override
    public List<DoctorEntity> findBySpecialty(DoctorSpecialty specialty) {
        return doctorRepository.findByDoctorSpecialty(specialty);
    }

    @Override
    @Transactional
    public void save(DoctorDto doctorDto) {
        DoctorEntity entity = mapper.toEntity(doctorDto);
        doctorRepository.save(entity);
    }

    @Override
    @Transactional
    public void update(DoctorDto doctorDto) {
        Integer id = doctorDto.getId();
        Optional<DoctorEntity> doctorOpt = doctorRepository.findById(id);
        if(doctorOpt.isPresent()){
            DoctorEntity doctorEntity = mapper.toEntity(doctorDto);
            doctorRepository.save(doctorEntity);
        }else {
            throw new NotFoundException("Doctor with this id not found");
        }
    }

    @Override
    @Transactional
    public void delete(Integer id)  {
        Optional<DoctorEntity> doctorOpt = doctorRepository.findById(id);
        if(doctorOpt.isPresent()){
            DoctorEntity doctorEntity = doctorOpt.get();
            doctorRepository.delete(doctorEntity);
        }else {
            throw new NotFoundException("Doctor with this id not found");
        }

    }
}
