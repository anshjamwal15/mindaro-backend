package com.dekhokaun.mindarobackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "zd2_menu")
@Getter
@Setter
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer code;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String icon;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String action;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String link;

    @Column(nullable = false)
    private Integer orderx;

    @Column(nullable = false)
    private Integer status;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime cdt;
}
