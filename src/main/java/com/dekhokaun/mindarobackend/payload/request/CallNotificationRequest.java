package com.dekhokaun.mindarobackend.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CallNotificationRequest {

    @NotBlank(message = "Type is required (video_call or voice_call)")
    private String type;

    @NotBlank(message = "Device token is required")
    @JsonProperty("device_token")
    private String deviceToken;

    @NotBlank(message = "Caller name is required")
    @JsonProperty("callerName")
    private String callerName;

    @NotBlank(message = "Caller ID is required")
    @JsonProperty("callerId")
    private String callerId;

    @NotBlank(message = "Room name is required")
    @JsonProperty("roomName")
    private String roomName;

    @NotBlank(message = "Call ID is required")
    @JsonProperty("callId")
    private String callId;
}
