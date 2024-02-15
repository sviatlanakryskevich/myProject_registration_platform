package com.tms.skv.registration_platform.service;

import com.tms.skv.registration_platform.entity.UserEntity;
import com.tms.skv.registration_platform.model.UserDto;
import com.tms.skv.registration_platform.model.UserUpdateDto;

public interface UserEntityService {
    UserEntity getById(Integer id);
    void save(UserDto user);
    void update(UserUpdateDto user);

}
