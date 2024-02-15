package com.tms.skv.registration_platform.exc;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AppointmentIsExistException extends RuntimeException{
    public AppointmentIsExistException(String message) {
        super(message);
    }
}
