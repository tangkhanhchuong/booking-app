package com.springboot.booking_app.module.user.service;

import com.springboot.booking_app.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User findUserById(UUID id);

    List<User> listUsers();

    User createUser();
}
