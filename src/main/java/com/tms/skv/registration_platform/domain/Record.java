package com.tms.skv.registration_platform.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Record {
    private LocalDateTime appointment;
    private boolean isFree;
}
