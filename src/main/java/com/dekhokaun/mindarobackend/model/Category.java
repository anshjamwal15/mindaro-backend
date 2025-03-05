package com.dekhokaun.mindarobackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "d3_category")
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime cdt;

    @UpdateTimestamp
    private LocalDateTime mdt;
}
