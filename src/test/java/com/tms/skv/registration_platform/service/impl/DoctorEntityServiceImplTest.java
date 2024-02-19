package com.tms.skv.registration_platform.service.impl;

import com.tms.skv.registration_platform.domain.DoctorSpecialty;
import com.tms.skv.registration_platform.entity.DoctorEntity;
import com.tms.skv.registration_platform.exc.NotFoundException;
import com.tms.skv.registration_platform.mapper.DoctorMapper;
import com.tms.skv.registration_platform.model.DoctorDto;
import com.tms.skv.registration_platform.repository.DoctorRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DoctorEntityServiceImplTest {
    @Mock
    private DoctorRepository doctorRepository;
    @Mock
    private DoctorMapper mapper;
    @InjectMocks
    private DoctorEntityServiceImpl doctorEntityService;

    @Test
    void testFindAll() {
        DoctorEntity doctor1 = DoctorEntity.builder()
                .doctorSpecialty(DoctorSpecialty.CARDIOLOGIST)
                .id(1)
                .experience(2.0)
                .fullName("Сойко Вера Владимировна")
                .build();
        DoctorEntity doctor2 = DoctorEntity.builder()
                .doctorSpecialty(DoctorSpecialty.ENDOCRINOLOGIST)
                .id(2)
                .experience(12.0)
                .fullName("Дуб Вера Владимировна")
                .build();
        when(doctorRepository.findAll()).thenReturn(List.of(doctor1, doctor2));

        List<DoctorEntity> all = doctorEntityService.findAll();

        Assertions.assertThat(all).hasSize(2);
        Assertions.assertThat(all).contains(doctor1, doctor2);
    }

    @Test
    void testFindById() {
        DoctorEntity doctor1 = DoctorEntity.builder()
                .doctorSpecialty(DoctorSpecialty.CARDIOLOGIST)
                .id(1)
                .experience(2.0)
                .fullName("Сойко Вера Владимировна")
                .build();
        when(doctorRepository.findById(1)).thenReturn(Optional.of(doctor1));
        DoctorEntity doctor = doctorEntityService.findById(1);
        Assertions.assertThat(doctor).isEqualTo(doctor1);
    }

    @Test
    void testFindByIdThrowException() {
        when(doctorRepository.findById(2)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> doctorEntityService.findById(2)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void testFindBySpecialty() {
        DoctorSpecialty cardiologist = DoctorSpecialty.CARDIOLOGIST;
        DoctorEntity doctor1 = DoctorEntity.builder()
                .doctorSpecialty(DoctorSpecialty.CARDIOLOGIST)
                .id(1)
                .experience(2.0)
                .fullName("Сойко Вера Владимировна")
                .build();
        DoctorEntity doctor2 = DoctorEntity.builder()
                .doctorSpecialty(DoctorSpecialty.CARDIOLOGIST)
                .id(2)
                .experience(12.0)
                .fullName("Дуб Вера Владимировна")
                .build();
        when(doctorRepository.findByDoctorSpecialty(cardiologist)).thenReturn(List.of(doctor1, doctor2));
        List<DoctorEntity> all = doctorEntityService.findBySpecialty(cardiologist);

        Assertions.assertThat(all).hasSize(2);
        Assertions.assertThat(all).contains(doctor1, doctor2);

    }

    @Test
    void testSave() {
        DoctorDto doctorDto = DoctorDto.builder()
                .doctorSpecialty(DoctorSpecialty.CARDIOLOGIST)
                .experience(2.0)
                .fullName("Сойко Вера Владимировна")
                .build();
        DoctorEntity doctorEntity = DoctorEntity.builder()
                .doctorSpecialty(DoctorSpecialty.CARDIOLOGIST)
                .experience(2.0)
                .fullName("Сойко Вера Владимировна")
                .build();
        when(mapper.toEntity(doctorDto)).thenReturn(doctorEntity);

        doctorEntityService.save(doctorDto);

        Mockito.verify(doctorRepository).save(doctorEntity);
    }

    @Test
    void testUpdate() {
        DoctorDto doctorDto = DoctorDto.builder()
                .id(1)
                .doctorSpecialty(DoctorSpecialty.THERAPIST)
                .experience(3.0)
                .fullName("Дуб Вера Владимировна")
                .build();
        DoctorEntity doctorEntity = DoctorEntity.builder()
                .doctorSpecialty(DoctorSpecialty.CARDIOLOGIST)
                .id(1)
                .experience(2.0)
                .fullName("Сойко Вера Владимировна")
                .build();
        DoctorEntity newDoctor = DoctorEntity.builder()
                .id(1)
                .doctorSpecialty(DoctorSpecialty.THERAPIST)
                .experience(3.0)
                .fullName("Дуб Вера Владимировна")
                .build();
        when(doctorRepository.findById(1)).thenReturn(Optional.of(doctorEntity));
        when(mapper.toEntity(doctorDto)).thenReturn(newDoctor);
        doctorEntityService.update(doctorDto);
        Mockito.verify(doctorRepository).save(newDoctor);
    }
    @Test
    void testUpdateThrowException() {
        DoctorDto doctorDto = DoctorDto.builder()
                .id(1)
                .doctorSpecialty(DoctorSpecialty.THERAPIST)
                .experience(3.0)
                .fullName("Дуб Вера Владимировна")
                .build();
        when(doctorRepository.findById(1)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> doctorEntityService.update(doctorDto)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void testDelete() {
        DoctorEntity doctor = DoctorEntity.builder()
                .doctorSpecialty(DoctorSpecialty.CARDIOLOGIST)
                .id(1)
                .experience(2.0)
                .fullName("Сойко Вера Владимировна")
                .build();
        when(doctorRepository.findById(1)).thenReturn(Optional.of(doctor));
        doctorEntityService.delete(1);
        Mockito.verify(doctorRepository).delete(doctor);
    }

    @Test
    void testDeleteThrowException() {
        when(doctorRepository.findById(2)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> doctorEntityService.delete(2)).isInstanceOf(NotFoundException.class);
        Mockito.verify(doctorRepository, Mockito.times(0)).delete(Mockito.any());
    }
}