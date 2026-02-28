package com.dekhokaun.mindarobackend.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MessageNotificationRequest {

    @NotBlank(message = "Device token is required")
    @JsonProperty("device_token")
    private String deviceToken;

    @NotBlank(message = "Sender name is required")
    @JsonProperty("senderName")
    private String senderName;

    @NotBlank(message = "Sender ID is required")
    @JsonProperty("senderId")
    private String senderId;

    @NotBlank(message = "Message is required")
    private String message;

    @NotBlank(message = "Chat room ID is required")
    @JsonProperty("chatRoomId")
    private String chatRoomId;
}
