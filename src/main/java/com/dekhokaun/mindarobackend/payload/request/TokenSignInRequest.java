package com.dekhokaun.mindarobackend.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TokenSignInRequest {
    @NotBlank
    private String jwtToken;
}
