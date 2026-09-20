package com.sahil.lostandfound.controller;

import com.sahil.lostandfound.entity.User;
import com.sahil.lostandfound.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return userRepository.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUserProfileById(@PathVariable Long id, @RequestBody User updatedData) {
        return userRepository.findById(id).map(existingUser -> {
            if (updatedData.getFullName() != null && !updatedData.getFullName().isBlank()) {
                existingUser.setFullName(updatedData.getFullName());
            }
            if (updatedData.getPhone() != null && !updatedData.getPhone().isBlank()) {
                existingUser.setPhone(updatedData.getPhone());
            }
            if (updatedData.getAvatar() != null && !updatedData.getAvatar().isBlank()) {
                existingUser.setAvatar(updatedData.getAvatar());
            }
            if (updatedData.getPassword() != null && !updatedData.getPassword().isBlank()) {
                existingUser.setPassword(updatedData.getPassword());
            }
            User saved = userRepository.save(existingUser);
            return ResponseEntity.ok(saved);
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/email/{email}")
    public ResponseEntity<User> updateUserProfileByEmail(@PathVariable String email, @RequestBody User updatedData) {
        return userRepository.findByEmail(email).map(existingUser -> {
            if (updatedData.getFullName() != null && !updatedData.getFullName().isBlank()) {
                existingUser.setFullName(updatedData.getFullName());
            }
            if (updatedData.getPhone() != null && !updatedData.getPhone().isBlank()) {
                existingUser.setPhone(updatedData.getPhone());
            }
            if (updatedData.getAvatar() != null && !updatedData.getAvatar().isBlank()) {
                existingUser.setAvatar(updatedData.getAvatar());
            }
            if (updatedData.getPassword() != null && !updatedData.getPassword().isBlank()) {
                existingUser.setPassword(updatedData.getPassword());
            }
            User saved = userRepository.save(existingUser);
            return ResponseEntity.ok(saved);
        }).orElseGet(() -> {
            // If user doesn't exist in DB yet, create user record
            User newUser = new User(
                updatedData.getFullName() != null ? updatedData.getFullName() : email,
                email,
                "password123",
                updatedData.getPhone(),
                updatedData.getAvatar(),
                "USER"
            );
            User saved = userRepository.save(newUser);
            return ResponseEntity.ok(saved);
        });
    }
}

