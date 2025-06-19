package com.example.userservice.service;
import com.example.userservice.dto.UserDTO;
import com.example.userservice.entity.User;

import java.util.List;

public interface UserService {
    int registerUser(UserDTO userDTO);
    List<User> getAllUsers();
    User getUserById(Long id);
    User findByEmail(String email);
    User updateUser(Long id, UserDTO dto);
    UserDTO loadUserDetailsByUsername(String email);
}

