package com.tms.skv.registration_platform.exc;

import com.tms.skv.registration_platform.model.UserDto;
import lombok.Data;

@Data
public class NotUniqueUserNameException extends RuntimeException {
    private UserDto user;
    public NotUniqueUserNameException(String message, UserDto userDto) {
        super(message);
        this.user = userDto;
    }
}
