package com.dekhokaun.mindarobackend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "a1_mentor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Mentor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String mentorfbid;

    private Integer umid;

    private Integer gcode;

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
    private String firstname;

    @Column(columnDefinition = "TEXT")
    private String lastname;

    @Column(columnDefinition = "TEXT")
    private String nationality;

    @Column(columnDefinition = "TEXT")
    private String photo;

    @Column(length = 2)
    private String gender;

    private LocalDate dob;

    @Column(columnDefinition = "TEXT")
    private String mainlanguage;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(columnDefinition = "TEXT")
    private String city;

    @Column(columnDefinition = "TEXT")
    private String state;

    private Integer pincode;

    private Integer totalexpyrs;

    private Integer timeweeklyhrs;

    @Column(columnDefinition = "TEXT")
    private String platforminfo;

    @Column(columnDefinition = "TEXT")
    private String qualification;

    @Column(columnDefinition = "TEXT")
    private String mainincome;

    @Column(columnDefinition = "TEXT")
    private String tw; // Twitter

    @Column(columnDefinition = "TEXT")
    private String linkedin;

    @Column(columnDefinition = "TEXT")
    private String yt; // YouTube

    @Column(columnDefinition = "TEXT")
    private String fb; // Facebook

    private Integer rating;

    private Integer ratingcount;

    private Integer rate;

    @Column(updatable = false)
    private LocalDateTime cdt = LocalDateTime.now(); // Created Timestamp

    private LocalDateTime mdt = LocalDateTime.now(); // Modified Timestamp

    @PreUpdate
    protected void onUpdate() {
        mdt = LocalDateTime.now();
    }
}
