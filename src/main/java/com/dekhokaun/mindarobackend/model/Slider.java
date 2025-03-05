package com.dekhokaun.mindarobackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "d1_slider")
@Getter
@Setter
public class Slider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime cdt;

    @UpdateTimestamp
    private LocalDateTime mdt;
}
