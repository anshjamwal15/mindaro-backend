package com.dekhokaun.mindarobackend.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OtpRequest {

    @NotBlank
    private String mobileOrEmail;

    @NotBlank
    private String userid;

    @NotBlank
    private String countryCode;

    @NotBlank
    private String otpType; // email or phone

    private String ip;

    private String otp;
}
