package com.dekhokaun.mindarobackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "x1_session")
@Getter
@Setter
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, unique = true, nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(columnDefinition = "TEXT")
    private String fbid;

    @Column(length = 50, nullable = false)
    private String userid;

    @Column(length = 100)
    private String ip;

    @Column(length = 200)
    private String browser;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime datetime;

    @Column(length = 250, nullable = false)
    private String gps;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime logintime;

    @Column
    private LocalDateTime logouttime;

    @Column(length = 25)
    private String logintype;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
