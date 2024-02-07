package com.tms.skv.registration_platform.service;

import com.tms.skv.registration_platform.model.UserDto;

public interface UserEntityService {
    void save(UserDto user);
    void update(UserDto user);
}
