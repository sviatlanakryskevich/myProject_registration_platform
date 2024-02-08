package com.tms.skv.registration_platform.exc;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class NotFoundException extends RuntimeException{
    public NotFoundException(String message) {
        super(message);
    }
}
