package com.dekhokaun.mindarobackend.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRequest {

    @NotBlank
    private String name;

    @Email
    private String email;

    private String mobile;
    private String country;

    private String password;
    private String token;

    @NotBlank
    private String method;
}
