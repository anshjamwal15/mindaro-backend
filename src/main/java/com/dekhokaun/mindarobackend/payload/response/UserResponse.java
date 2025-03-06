package com.dekhokaun.mindarobackend.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class UserResponse {

    private String name;
    private String email;
    private String mobile;
    private String country;
    private String userType;
}
