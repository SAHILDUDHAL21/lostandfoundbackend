package com.sahil.lostandfound.controller;

import com.sahil.lostandfound.config.JwtUtil;
import com.sahil.lostandfound.dto.AuthResponse;
import com.sahil.lostandfound.dto.LoginRequest;
import com.sahil.lostandfound.entity.User;
import com.sahil.lostandfound.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body(new AuthResponse(false, "Email already registered!", null, null));
        }

        if (user.getAvatar() == null || user.getAvatar().isBlank()) {
            user.setAvatar("data:image/svg+xml;utf8,<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'><circle cx='50' cy='50' r='50' fill='%2330281e'/><circle cx='50' cy='40' r='20' fill='%23faf5e9'/><path d='M20,85 C20,65 35,60 50,60 C65,60 80,65 80,85 Z' fill='%23faf5e9'/></svg>");
        }

        User savedUser = userRepository.save(user);
        String token = jwtUtil.generateToken(savedUser.getEmail());

        return ResponseEntity.ok(new AuthResponse(true, "Registration successful!", token, savedUser));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        Optional<User> userOpt = userRepository.findByEmail(loginRequest.getEmail());

        if (userOpt.isPresent() && userOpt.get().getPassword().equals(loginRequest.getPassword())) {
            User user = userOpt.get();
            String token = jwtUtil.generateToken(user.getEmail());
            return ResponseEntity.ok(new AuthResponse(true, "Login successful!", token, user));
        }

        return ResponseEntity.badRequest().body(new AuthResponse(false, "Invalid email or password!", null, null));
    }

    @GetMapping("/verify")
    public ResponseEntity<AuthResponse> verifyToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                String email = jwtUtil.extractEmail(token);
                Optional<User> userOpt = userRepository.findByEmail(email);
                if (userOpt.isPresent() && jwtUtil.validateToken(token, email)) {
                    return ResponseEntity.ok(new AuthResponse(true, "Token valid!", token, userOpt.get()));
                }
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(new AuthResponse(false, "Invalid token!", null, null));
            }
        }
        return ResponseEntity.badRequest().body(new AuthResponse(false, "Missing Authorization header!", null, null));
    }
}
