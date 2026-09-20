package com.sahil.lostandfound.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "scammers")
public class ScammerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String username;

    @Column(columnDefinition = "TEXT")
    private String avatar;

    private int failedClaimsCount;

    @Column(columnDefinition = "TEXT")
    private String reason;

    private String status;

    public ScammerProfile() {}

    public ScammerProfile(String fullName, String username, String avatar, int failedClaimsCount, String reason, String status) {
        this.fullName = fullName;
        this.username = username;
        this.avatar = avatar;
        this.failedClaimsCount = failedClaimsCount;
        this.reason = reason;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public int getFailedClaimsCount() { return failedClaimsCount; }
    public void setFailedClaimsCount(int failedClaimsCount) { this.failedClaimsCount = failedClaimsCount; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
