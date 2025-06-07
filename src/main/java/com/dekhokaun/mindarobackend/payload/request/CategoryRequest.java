package com.dekhokaun.mindarobackend.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequest {

    @NotBlank
    private String name;

    private String description;
}
