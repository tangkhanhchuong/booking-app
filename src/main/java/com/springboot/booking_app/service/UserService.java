package com.springboot.booking_app.service;

import com.springboot.booking_app.model.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User findUserById(UUID id);

    List<User> listUsers();

    User createUser();
}
