package com.example.user_service.controller;

import com.example.user_service.DTO.LoginRequest;
import com.example.user_service.DTO.UserRegistrationRequest;
import com.example.user_service.model.User;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private UserService userService;
    private UserRepository userRepository;
    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
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
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = userRepository.findByEmail(request.email()).orElse(null);

        if (user == null) {
            return ResponseEntity.status(401).body("User not found");
        }
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            return ResponseEntity.status(401).body("Wrong password");
        }
        String token = Jwts.builder()
                .setSubject(user.getEmail())
                .claim("userId", user.getId())
                .claim("name", user.getName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(Keys.hmacShaKeyFor("myverysecretkeythatissuperlongandsecure123".getBytes()))
                .compact();

        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpSession session) {
        try {
            session.invalidate();
            System.out.println("Logout successful");
            return ResponseEntity.ok("{\"message\": \"Logout successful\"}");

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("{\"message\": \"Logout failed due to server error\"}");
        }
    }
}