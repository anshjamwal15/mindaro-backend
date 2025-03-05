package com.dekhokaun.mindarobackend.payload.response;

import lombok.Data;

@Data
public class MentorResponse {

    private String name;
    private String category;
    private int experience;
    private double rating;
}
