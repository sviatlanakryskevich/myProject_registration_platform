package com.tms.skv.registration_platform.exc;

import com.tms.skv.registration_platform.entity.DoctorEntity;
import com.tms.skv.registration_platform.model.DoctorDto;
import lombok.Data;

@Data
public class AppointmentIsExistException extends RuntimeException{
    public AppointmentIsExistException(String message) {
        super(message);
    }
}
