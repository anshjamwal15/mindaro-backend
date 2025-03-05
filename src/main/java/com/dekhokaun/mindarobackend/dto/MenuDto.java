package com.dekhokaun.mindarobackend.dto;

import lombok.Data;

@Data
public class MenuDto {
    private Long id;
    private String title;
    private String description;
    private Double price;
}
