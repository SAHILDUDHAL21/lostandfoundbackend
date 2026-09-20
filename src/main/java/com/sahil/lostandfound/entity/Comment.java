package com.sahil.lostandfound.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long itemId;

    @Column(name = "user_name")
    private String user;

    @Column(columnDefinition = "TEXT")
    private String avatar;

    private String time;

    @Column(columnDefinition = "TEXT")
    private String text;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Comment() {}

    public Comment(Long itemId, String user, String avatar, String time, String text) {
        this.itemId = itemId;
        this.user = user;
        this.avatar = avatar;
        this.time = time;
        this.text = text;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getItemId() { return itemId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }

    public String getUser() { return user; }
    public void setUser(String user) { this.user = user; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
