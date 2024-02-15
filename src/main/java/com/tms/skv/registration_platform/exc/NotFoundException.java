package com.tms.skv.registration_platform.exc;

import lombok.Data;
@Data
public class NotFoundException extends RuntimeException{
    public NotFoundException(String message) {
        super(message);
    }
}
