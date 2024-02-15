package com.tms.skv.registration_platform.exc;

import lombok.Data;

@Data
public class AppointmentIsExistException extends RuntimeException{
    public AppointmentIsExistException(String message) {
        super(message);
    }
}
