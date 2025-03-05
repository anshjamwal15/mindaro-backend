package com.dekhokaun.mindarobackend.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SessionRequest {

    @NotBlank
    private String userId;

    @NotBlank
    private String device;
}
