package com.sahil.lostandfound.controller;

import com.sahil.lostandfound.entity.Claim;
import com.sahil.lostandfound.entity.Comment;
import com.sahil.lostandfound.entity.Item;
import com.sahil.lostandfound.entity.User;
import com.sahil.lostandfound.repository.ClaimRepository;
import com.sahil.lostandfound.repository.CommentRepository;
import com.sahil.lostandfound.repository.ItemRepository;
import com.sahil.lostandfound.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ClaimRepository claimRepository;

    @Autowired
    private CommentRepository commentRepository;

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
            boolean nameChanged = updatedData.getFullName() != null && !updatedData.getFullName().isBlank() && !updatedData.getFullName().equals(existingUser.getFullName());
            boolean avatarChanged = updatedData.getAvatar() != null && !updatedData.getAvatar().isBlank() && !updatedData.getAvatar().equals(existingUser.getAvatar());

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

            if (nameChanged || avatarChanged) {
                syncUserDataByEmail(existingUser.getEmail(), existingUser.getFullName(), existingUser.getAvatar());
            }

            return ResponseEntity.ok(saved);
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/email/{email}")
    public ResponseEntity<User> updateUserProfileByEmail(@PathVariable String email, @RequestBody User updatedData) {
        return userRepository.findByEmail(email).map(existingUser -> {
            boolean nameChanged = updatedData.getFullName() != null && !updatedData.getFullName().isBlank() && !updatedData.getFullName().equals(existingUser.getFullName());
            boolean avatarChanged = updatedData.getAvatar() != null && !updatedData.getAvatar().isBlank() && !updatedData.getAvatar().equals(existingUser.getAvatar());

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

            if (nameChanged || avatarChanged) {
                syncUserDataByEmail(saved.getEmail(), saved.getFullName(), saved.getAvatar());
            }

            return ResponseEntity.ok(saved);
        }).orElseGet(() -> {
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

    private void syncUserDataByEmail(String email, String newName, String newAvatar) {
        if (email == null || email.isBlank()) return;

        // Sync Items reported by this email
        List<Item> items = itemRepository.findByFinderEmailOrderByIdDesc(email);
        for (Item item : items) {
            if (newName != null && !newName.isBlank()) item.setFinderName(newName);
            if (newAvatar != null && !newAvatar.isBlank()) item.setFinderAvatar(newAvatar);
        }
        if (!items.isEmpty()) itemRepository.saveAll(items);

        // Sync Claims made by this email
        List<Claim> claims = claimRepository.findByUserEmailOrderByIdDesc(email);
        for (Claim claim : claims) {
            if (newName != null && !newName.isBlank()) claim.setUser(newName);
            if (newAvatar != null && !newAvatar.isBlank()) claim.setAvatar(newAvatar);
        }
        if (!claims.isEmpty()) claimRepository.saveAll(claims);

        // Sync Comments posted by this email
        List<Comment> comments = commentRepository.findByUserEmailOrderByIdAsc(email);
        for (Comment comment : comments) {
            if (newName != null && !newName.isBlank()) comment.setUser(newName);
            if (newAvatar != null && !newAvatar.isBlank()) comment.setAvatar(newAvatar);
        }
        if (!comments.isEmpty()) commentRepository.saveAll(comments);
    }
}

