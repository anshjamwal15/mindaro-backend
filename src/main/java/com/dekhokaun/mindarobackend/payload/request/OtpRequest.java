package com.dekhokaun.mindarobackend.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OtpRequest {

    @NotBlank
    private String mobileOrEmail;
}
