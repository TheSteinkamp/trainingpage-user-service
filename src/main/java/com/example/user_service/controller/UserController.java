package com.example.user_service.controller;

import com.example.user_service.DTO.UserRegistrationRequest;
import com.example.user_service.model.User;
import com.example.user_service.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
 private UserService userService;

 public UserController(UserService userService) {
     this.userService = userService;
 }

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/new")
    public User createUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationRequest request) {

        if (!userService.isUserFieldsFilledAndCorrect(request)) {
            return ResponseEntity.badRequest().body("{\"message\": \"Invalid or missing credentials.\"}");
        }

        try {
            User user = new User(request.name(), request.email(), request.password());

            user = userService.createUser(user);

            log.info("New user registered: {} - {}", user.getEmail(), user.getName());
            return new ResponseEntity<>("{\"message\": \"User registered successfully.\"}", HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            log.error("Error during user registration: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }catch (Exception e) {
            log.error("Failed to register new user", e);
            return ResponseEntity.internalServerError().body("{\"message\": \"Registration failed due to server error.\"}");
        }
    }
}
