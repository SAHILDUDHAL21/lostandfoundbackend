package com.sahil.lostandfound.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // "FOUND"
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String location;
    private String timeAgo;

    @Column(columnDefinition = "TEXT")
    private String image;

    private String finderName;
    private String finderHandle;

    @Column(columnDefinition = "TEXT")
    private String finderAvatar;

    private LocalDateTime createdAt;

    @Transient
    private List<Claim> claims = new ArrayList<>();

    @Transient
    private List<Comment> comments = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Item() {}

    public Item(String type, String title, String description, String location, String timeAgo, String image, String finderName, String finderHandle, String finderAvatar) {
        this.type = type;
        this.title = title;
        this.description = description;
        this.location = location;
        this.timeAgo = timeAgo;
        this.image = image;
        this.finderName = finderName;
        this.finderHandle = finderHandle;
        this.finderAvatar = finderAvatar;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getTimeAgo() { return timeAgo; }
    public void setTimeAgo(String timeAgo) { this.timeAgo = timeAgo; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getFinderName() { return finderName; }
    public void setFinderName(String finderName) { this.finderName = finderName; }

    public String getFinderHandle() { return finderHandle; }
    public void setFinderHandle(String finderHandle) { this.finderHandle = finderHandle; }

    public String getFinderAvatar() { return finderAvatar; }
    public void setFinderAvatar(String finderAvatar) { this.finderAvatar = finderAvatar; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<Claim> getClaims() { return claims; }
    public void setClaims(List<Claim> claims) { this.claims = claims; }

    public List<Comment> getComments() { return comments; }
    public void setComments(List<Comment> comments) { this.comments = comments; }
}
