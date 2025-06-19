package com.example.userservice.service.impl;
import com.example.userservice.dto.UserDTO;
import com.example.userservice.entity.Role;
import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.UserService;
import com.example.userservice.utill.JwtUtil;
import com.example.userservice.utill.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtTokenUtil;



    private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().toString()));
        return authorities;
    }
    public UserDTO loadUserDetailsByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        return modelMapper.map(user,UserDTO.class);
    }
    @Override
    public int registerUser(UserDTO dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            return VarList.Not_Acceptable;
        } else {
            User user = modelMapper.map(dto, User.class);

            // Encrypt the password
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            user.setPassword(passwordEncoder.encode(dto.getPassword()));

            // Set default role if not provided
            if (user.getRole() == null) {
                user.setRole(Role.USER);
            }
            userRepository.save(user);
            return VarList.Created;
        }

    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    @Override
    public User updateUser(Long id, UserDTO dto) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        modelMapper.map(dto, user);
        return userRepository.save(user);

    }


    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), // Use email as the username
                user.getPassword(), // Password
                getAuthority(user) // Convert role to GrantedAuthority
        );
    }
}
