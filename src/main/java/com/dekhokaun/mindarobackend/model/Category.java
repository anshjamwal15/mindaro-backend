package com.dekhokaun.mindarobackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "d3_category")
@Getter
@Setter
public class Category {
    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String fbid;

    @Column(nullable = false)
    private Integer code;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer prede;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String icon;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String image;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;

    @Column(length = 5, nullable = false)
    private String language;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String action;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String link;

    @Column(nullable = false)
    private Integer orderx;

    @Column(nullable = false)
    private Integer status;

    @ManyToMany(mappedBy = "categories")
    private List<Mentor> mentors;

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
