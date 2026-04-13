package com.example.user_service.service;

import com.example.user_service.DTO.UserRegistrationRequest;
import com.example.user_service.model.User;
import com.example.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public boolean isUserFieldsFilledAndCorrect(UserRegistrationRequest request) {
        if (request.email().trim().length() < 5) {
            return false;
        } else if (request.password().trim().length() < 5) {
            return false;
        }else if (request.name().trim().length() < 5) {
            return false;
        }
        return true;
    }

    public User createUser(User user) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalStateException("Användarnamnet är upptaget");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
