package com.dekhokaun.mindarobackend.dto;

import lombok.Data;

@Data
public class OtpDto {
    private String email;
    private String otpCode;
}
