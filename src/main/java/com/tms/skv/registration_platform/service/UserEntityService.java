package com.tms.skv.registration_platform.service;

import com.tms.skv.registration_platform.entity.OrderEntity;
import com.tms.skv.registration_platform.model.UserDto;

import java.util.List;

public interface UserEntityService {
    void save(UserDto user);
    void update(UserDto user);

}
