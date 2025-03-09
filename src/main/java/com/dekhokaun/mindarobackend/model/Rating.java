package com.dekhokaun.mindarobackend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "a1_ratings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rating {
    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "mentor_id", nullable = false)
    private Mentor mentor;

    @Column(nullable = false)
    private Integer rating;

    private String review;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isApproved = false;

    private String userIp;
    private String deviceInfo;

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
