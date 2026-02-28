package com.dekhokaun.mindarobackend.service;

import com.dekhokaun.mindarobackend.payload.request.CallNotificationRequest;
import com.dekhokaun.mindarobackend.payload.request.MessageNotificationRequest;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    public void sendCallNotification(CallNotificationRequest request) {
        log.info("=== CALL NOTIFICATION REQUEST ===");
        log.info("Type: {}", request.getType());
        log.info("Device Token: {}", maskToken(request.getDeviceToken()));
        log.info("Caller Name: {}", request.getCallerName());
        log.info("Caller ID: {}", request.getCallerId());
        log.info("Room Name: {}", request.getRoomName());
        log.info("Call ID: {}", request.getCallId());
        
        try {
            // Data payload
            Map<String, String> data = new HashMap<>();
            data.put("type", request.getType());
            data.put("callerName", request.getCallerName());
            data.put("callerId", request.getCallerId());
            data.put("roomName", request.getRoomName());
            data.put("callId", request.getCallId());

            // Notification title based on call type
            String title = request.getType().equals("video_call") 
                    ? "Incoming Video Call" 
                    : "Incoming Voice Call";

            // Notification payload
            Notification notification = Notification.builder()
                    .setTitle(title)
                    .setBody(String.format("%s is calling...", request.getCallerName()))
                    .build();

            // Android-specific configuration
            AndroidConfig androidConfig = AndroidConfig.builder()
                    .setPriority(AndroidConfig.Priority.HIGH)
                    .setNotification(AndroidNotification.builder()
                            .setChannelId("calls")
                            .setSound("default")
                            .setPriority(AndroidNotification.Priority.MAX)
                            .setDefaultVibrateTimings(false)
                            .build())
                    .build();

            // APNS (iOS) configuration
            ApnsConfig apnsConfig = ApnsConfig.builder()
                    .putHeader("apns-priority", "10")
                    .setAps(Aps.builder()
                            .setSound("default")
                            .setBadge(1)
                            .build())
                    .build();

            // Build the complete message
            Message message = Message.builder()
                    .setToken(request.getDeviceToken())
                    .setNotification(notification)
                    .putAllData(data)
                    .setAndroidConfig(androidConfig)
                    .setApnsConfig(apnsConfig)
                    .build();

            log.info("Sending Firebase message with data: {}", data);
            String response = FirebaseMessaging.getInstance().send(message);
            
            log.info("=== CALL NOTIFICATION RESPONSE ===");
            log.info("Firebase Response: {}", response);
            log.info("Status: SUCCESS");
            log.info("Notification Type: {}", request.getType());
            log.info("===================================");

        } catch (FirebaseMessagingException e) {
            log.error("=== CALL NOTIFICATION FAILED ===");
            log.error("Error Code: {}", e.getErrorCode());
            log.error("Error Message: {}", e.getMessage());
            log.error("Messaging Error Code: {}", e.getMessagingErrorCode());
            log.error("HTTP Status Code: {}", e.getHttpResponse() != null ? e.getHttpResponse().getStatusCode() : "N/A");
            log.error("Request Type: {}", request.getType());
            log.error("Device Token (masked): {}", maskToken(request.getDeviceToken()));
            log.error("================================", e);
            throw new RuntimeException("Failed to send call notification: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("=== UNEXPECTED ERROR ===");
            log.error("Error: {}", e.getMessage());
            log.error("Request Type: {}", request.getType());
            log.error("========================", e);
            throw new RuntimeException("Unexpected error during call notification send: " + e.getMessage(), e);
        }
    }

    public void sendMessageNotification(MessageNotificationRequest request) {
        log.info("=== MESSAGE NOTIFICATION REQUEST ===");
        log.info("Device Token: {}", maskToken(request.getDeviceToken()));
        log.info("Sender Name: {}", request.getSenderName());
        log.info("Sender ID: {}", request.getSenderId());
        log.info("Message: {}", request.getMessage());
        log.info("Chat Room ID: {}", request.getChatRoomId());
        
        try {
            // Data payload
            Map<String, String> data = new HashMap<>();
            data.put("type", "message");
            data.put("senderName", request.getSenderName());
            data.put("senderId", request.getSenderId());
            data.put("message", request.getMessage());
            data.put("chatRoomId", request.getChatRoomId());

            // Notification payload
            Notification notification = Notification.builder()
                    .setTitle(request.getSenderName())
                    .setBody(request.getMessage())
                    .build();

            // Android-specific configuration
            AndroidConfig androidConfig = AndroidConfig.builder()
                    .setPriority(AndroidConfig.Priority.HIGH)
                    .setNotification(AndroidNotification.builder()
                            .setChannelId("messages")
                            .setSound("default")
                            .setPriority(AndroidNotification.Priority.HIGH)
                            .setDefaultVibrateTimings(false)
                            .build())
                    .build();

            // APNS (iOS) configuration
            ApnsConfig apnsConfig = ApnsConfig.builder()
                    .putHeader("apns-priority", "10")
                    .setAps(Aps.builder()
                            .setSound("default")
                            .setBadge(1)
                            .build())
                    .build();

            // Build the complete message
            Message message = Message.builder()
                    .setToken(request.getDeviceToken())
                    .setNotification(notification)
                    .putAllData(data)
                    .setAndroidConfig(androidConfig)
                    .setApnsConfig(apnsConfig)
                    .build();

            log.info("Sending Firebase message with data: {}", data);
            String response = FirebaseMessaging.getInstance().send(message);
            
            log.info("=== MESSAGE NOTIFICATION RESPONSE ===");
            log.info("Firebase Response: {}", response);
            log.info("Status: SUCCESS");
            log.info("Sender: {}", request.getSenderName());
            log.info("Chat Room: {}", request.getChatRoomId());
            log.info("=====================================");

        } catch (FirebaseMessagingException e) {
            log.error("=== MESSAGE NOTIFICATION FAILED ===");
            log.error("Error Code: {}", e.getErrorCode());
            log.error("Error Message: {}", e.getMessage());
            log.error("Sender: {}", request.getSenderName());
            log.error("===================================", e);
            throw new RuntimeException("Failed to send message notification: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("=== UNEXPECTED ERROR ===");
            log.error("Error: {}", e.getMessage());
            log.error("Sender: {}", request.getSenderName());
            log.error("========================", e);
            throw new RuntimeException("Unexpected error during message notification send: " + e.getMessage(), e);
        }
    }

    private String maskToken(String token) {
        if (token == null || token.length() < 20) {
            return "***";
        }
        return token.substring(0, 10) + "..." + token.substring(token.length() - 10);
    }
}