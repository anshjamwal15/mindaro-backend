package com.dekhokaun.mindarobackend.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "email|mobile", message = "otpType must be either 'email' or 'mobile'")
    private String otpType; // email or mobile

    private String ip;

    private String otp;
}
