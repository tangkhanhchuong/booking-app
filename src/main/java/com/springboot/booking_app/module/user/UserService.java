package com.springboot.booking_app.module.user;

import com.springboot.booking_app.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findUserByUsername(String username);
}
