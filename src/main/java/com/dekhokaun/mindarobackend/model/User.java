package com.dekhokaun.mindarobackend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "a1_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

    private Integer umid;

    private Integer gcode;

    @Column(columnDefinition = "TEXT")
    private String userfbid;

    @Column(columnDefinition = "TEXT")
    private String mentorfbid;

    @Column(columnDefinition = "TEXT")
    private String name;

    @Column(columnDefinition = "TEXT")
    private String email;

    private Long mobile;

    @Column(length = 5)
    private String country;

    @Column(columnDefinition = "TINYTEXT")
    private String pwd;

    @Column(length = 11)
    private String status;

    private Integer ulevel;

    @Column(length = 7)
    private String utype;

    private Integer parentid;

    @Column(columnDefinition = "TEXT")
    private String token;

    @Column(precision = 5, scale = 2)
    private BigDecimal rating;

    private Integer ratingcount;

    @Column(columnDefinition = "TEXT")
    private String howtoknow;

    @ManyToOne
    @JoinColumn(name = "mentor_id")
    private Mentor mentor;

    @OneToMany(mappedBy = "user")
    private List<Rating> ratings;

    @OneToMany(mappedBy = "user")
    private List<Transaction> transactions;

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
