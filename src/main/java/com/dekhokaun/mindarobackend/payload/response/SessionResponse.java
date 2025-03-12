package com.dekhokaun.mindarobackend.payload.response;

import lombok.Data;

@Data
public class SessionResponse {
    private String id;
    private String userId;
    private String startTime;
    private String endTime;
}
