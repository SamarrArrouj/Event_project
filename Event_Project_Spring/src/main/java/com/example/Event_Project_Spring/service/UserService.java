package com.example.Event_Project_Spring.service;

import com.example.Event_Project_Spring.entities.User;

import java.util.List;

public interface UserService {
    User addUser(User user);
    List<User> read();
    User updateUser(Long id,User user);
    String deleteUser(Long id);
    User findByUsername(String username);
    User findById(Long id);

}
