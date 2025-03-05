package com.dekhokaun.mindarobackend.payload.response;

import lombok.Data;

@Data
public class SessionResponse {

    private String sessionId;
    private boolean isNew;
}
