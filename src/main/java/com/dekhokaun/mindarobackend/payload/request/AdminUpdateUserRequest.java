package com.dekhokaun.mindarobackend.payload.request;

import lombok.Data;

@Data
public class AdminUpdateUserRequest {
    private String name;
    private String mobile;
    private String password;
    private String country;
}