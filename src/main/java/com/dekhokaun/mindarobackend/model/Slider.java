package com.dekhokaun.mindarobackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "d1_slider")
@Getter
@Setter
public class Slider {
    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

    @Column(columnDefinition = "TEXT")
    private String fbid;

    private LocalDateTime datefrom;

    private LocalDateTime dateto;

    @Column(length = 11)
    private String lot;

    private Integer code;

    @Column(columnDefinition = "TEXT")
    private String imagename;

    @Column(columnDefinition = "TEXT")
    private String action;

    @Column(columnDefinition = "TEXT")
    private String link;

    private Integer orderx;

    private Integer status;

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
