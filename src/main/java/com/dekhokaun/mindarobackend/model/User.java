package com.dekhokaun.mindarobackend.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "a1_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer umid;

    private Integer gcode;

    @Column(columnDefinition = "TEXT")
    private String userfbid;

    private Integer mentorid;

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
    private String createdAt;

    @Column(updatable = false)
    private LocalDateTime cdt = LocalDateTime.now();

    private LocalDateTime mdt = LocalDateTime.now();

    @Column(columnDefinition = "TEXT")
    private String token;

    @Column(precision = 5, scale = 2)
    private BigDecimal rating;

    private Integer ratingcount;

    @Column(columnDefinition = "TEXT")
    private String howtoknow;

    @PreUpdate
    protected void onUpdate() {
        mdt = LocalDateTime.now();
    }
}
