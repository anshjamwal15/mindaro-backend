package com.dekhokaun.mindarobackend.controller;

import com.dekhokaun.mindarobackend.payload.request.CallNotificationRequest;
import com.dekhokaun.mindarobackend.payload.request.MessageNotificationRequest;
import com.dekhokaun.mindarobackend.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Push Notifications", description = "Firebase push notification APIs")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "Send call notification", description = "Send video/voice call notification via Firebase")
    @PostMapping("/call")
    public ResponseEntity<String> sendCallNotification(@Valid @RequestBody CallNotificationRequest request) {
        notificationService.sendCallNotification(request);
        return ResponseEntity.ok("Call notification sent successfully");
    }

    @Operation(summary = "Send message notification", description = "Send message notification via Firebase")
    @PostMapping("/message")
    public ResponseEntity<String> sendMessageNotification(@Valid @RequestBody MessageNotificationRequest request) {
        notificationService.sendMessageNotification(request);
        return ResponseEntity.ok("Message notification sent successfully");
    }
}
