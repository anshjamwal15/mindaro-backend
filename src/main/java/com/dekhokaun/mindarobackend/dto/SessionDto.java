package com.dekhokaun.mindarobackend.dto;

import lombok.Data;

@Data
public class SessionDto {
    private Long id;
    private String userId;
    private String startTime;
    private String endTime;
}
