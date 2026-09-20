package com.sahil.lostandfound.controller;

import com.sahil.lostandfound.entity.Comment;
import com.sahil.lostandfound.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "*")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping("/item/{itemId}")
    public List<Comment> getCommentsByItem(@PathVariable Long itemId) {
        return commentRepository.findByItemIdOrderByIdAsc(itemId);
    }

    @PostMapping
    public ResponseEntity<Comment> addComment(@RequestBody Comment comment) {
        if (comment.getTime() == null || comment.getTime().isBlank()) {
            comment.setTime("Just now");
        }
        Comment savedComment = commentRepository.save(comment);
        return ResponseEntity.ok(savedComment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        if (commentRepository.existsById(id)) {
            commentRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

