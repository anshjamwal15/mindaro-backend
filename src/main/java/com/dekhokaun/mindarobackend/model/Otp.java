package com.dekhokaun.mindarobackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "x2_otp")
@Getter
@Setter
public class Otp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50, nullable = false)
    private String userid;

    @Column(length = 11, nullable = false)
    private String otpType;

    @Column(length = 20, nullable = false)
    private String countryCodeMobile;

    @Column(length = 20, nullable = false)
    private String mobile;

    @Column(length = 20, nullable = false)
    private String otp;

    @Column(length = 100, nullable = false)
    private String ip;

    @Column(length = 50, nullable = false)
    private String smsCounter;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String smsResponse;

    @Column(length = 11, nullable = false)
    private String smsapi;

    @Column(length = 20, nullable = false)
    private String status;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime cdt;
}
