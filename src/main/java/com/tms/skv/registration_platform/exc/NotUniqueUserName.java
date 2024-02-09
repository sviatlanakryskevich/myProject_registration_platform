package com.tms.skv.registration_platform.exc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class NotUniqueUserName extends RuntimeException {
    public NotUniqueUserName(String message) {
        super(message);
    }
}
