package com.dekhokaun.mindarobackend.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class MentorRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String category;

    @Positive
    private int experience;

    private double rating;
}
