package com.tms.skv.registration_platform.exc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotUniqueUserName extends RuntimeException {
    private String errorMessage;
}
