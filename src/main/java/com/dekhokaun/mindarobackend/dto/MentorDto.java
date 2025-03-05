package com.dekhokaun.mindarobackend.dto;

import lombok.Data;

@Data
public class MentorDto {
    private Long id;
    private String name;
    private String expertise;
    private String email;
    private String phone;
}
