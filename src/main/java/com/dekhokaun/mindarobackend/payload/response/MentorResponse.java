package com.dekhokaun.mindarobackend.payload.response;

import lombok.Data;

@Data
public class MentorResponse {
    private String id;
    private String name;
    private String category;
    private int experience;
    private double rating;
    private String email;
    private String phone;
}
