package com.example.user_service.service;

import com.example.user_service.model.User;
import com.example.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User getUserById(@PathVariable Long id) {
        return userRepository.findById(id).get();
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public void deleteUserById(@PathVariable Long id) {
        userRepository.deleteById(id);
    }

    public User addUser(@RequestBody User user) {
        return userRepository.save(user);
    }
}
