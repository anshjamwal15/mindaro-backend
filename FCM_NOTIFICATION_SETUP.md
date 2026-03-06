# Firebase Cloud Messaging (FCM) Notification Setup Guide

## ✅ Current Implementation Status

Your backend is **already fully implemented** with Firebase Admin SDK! Here's what's in place:

### Implemented Components:
1. ✅ Firebase Admin SDK dependency in `build.gradle`
2. ✅ `FirebaseConfig.java` - Initializes Firebase on startup
3. ✅ `NotificationService.java` - Sends FCM notifications
4. ✅ `NotificationController.java` - REST endpoints for notifications
5. ✅ `CallNotificationRequest.java` - Request DTO with validation
6. ✅ Service account key file location configured

## 🔧 Setup Instructions

### Step 1: Get Firebase Service Account Key

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Select your project
3. Click the gear icon ⚙️ → **Project Settings**
4. Go to **Service Accounts** tab
5. Click **Generate New Private Key**
6. Download the JSON file

### Step 2: Configure Service Account Key

1. Rename the downloaded file to `service-account-key.json`
2. Place it in: `src/main/resources/service-account-key.json`
3. The file is already in `.gitignore` (don't commit it!)

### Step 3: Restart Your Application

```bash
./gradlew bootRun
```

Or if using an IDE, restart the Spring Boot application.

## 📡 API Endpoints

### Send Call Notification

**Endpoint:** `POST /api/notifications/call`

**Request Body:**
```json
{
  "type": "VIDEO_CALL",
  "device_token": "eUUu8zqzTpCRGP9nUPqBzf:APA91bF6mn_6VkTkUF4uWewhY3U...",
  "callerName": "John Doe",
  "callerId": "user_123",
  "roomName": "room_456",
  "callId": "call_789"
}
```

**Supported Types:**
- `VIDEO_CALL` or `video_call`
- `VOICE_CALL` or `voice_call`

**cURL Example:**
```bash
curl -X POST 'http://192.168.1.9:3000/api/notifications/call' \
  -H 'Content-Type: application/json' \
  -d '{
    "type": "VIDEO_CALL",
    "device_token": "YOUR_DEVICE_TOKEN_HERE",
    "callerName": "Test Caller",
    "callerId": "test_123",
    "roomName": "test_room",
    "callId": "test_call_123"
  }'
```

**Success Response:**
```
200 OK
"Call notification sent successfully"
```

### Send Message Notification

**Endpoint:** `POST /api/notifications/message`

**Request Body:**
```json
{
  "device_token": "eUUu8zqzTpCRGP9nUPqBzf:APA91bF6mn_6VkTkUF4uWewhY3U...",
  "senderName": "Jane Smith",
  "senderId": "user_456",
  "message": "Hello! How are you?",
  "chatRoomId": "room_789"
}
```

## 🔍 Notification Payload Details

### Android Configuration
- **Priority:** HIGH
- **Channel ID:** `calls` (for call notifications) or `messages` (for message notifications)
- **Sound:** default
- **Vibration:** default timings

### iOS (APNS) Configuration
- **Priority:** 10 (highest)
- **Sound:** default
- **Badge:** 1

### Data Payload (Call Notification)
```json
{
  "type": "VIDEO_CALL",
  "callerName": "John Doe",
  "callerId": "user_123",
  "roomName": "room_456",
  "callId": "call_789"
}
```

## 🐛 Troubleshooting

### Issue: "Failed to initialize Firebase"

**Cause:** Service account key file not found or invalid

**Solution:**
1. Verify file exists: `src/main/resources/service-account-key.json`
2. Check file is valid JSON
3. Ensure it's from the correct Firebase project
4. Restart the application

### Issue: "Invalid registration token"

**Cause:** Device token is expired or invalid

**Solution:**
1. User needs to log in again to get a fresh FCM token
2. Verify the token is from the same Firebase project
3. Check token hasn't been deleted from Firebase Console

### Issue: "Requested entity was not found"

**Cause:** Service account key is from a different Firebase project

**Solution:**
1. Verify `google-services.json` in Android app matches backend project
2. Download service account key from the correct project
3. Ensure project ID matches

### Issue: Notification sent but not received

**Possible Causes:**
1. Device has no internet connection
2. App doesn't have notification permissions
3. Battery optimization is killing the app (Android)
4. Notification channel not created in app (Android)

**Solution:**
1. Check device internet connection
2. Verify notification permissions in app settings
3. Disable battery optimization for the app
4. Ensure app creates notification channels on startup

## 📊 Logging

The service logs detailed information for debugging:

### Success Log:
```
=== CALL NOTIFICATION RESPONSE ===
Firebase Response: projects/your-project/messages/0:1234567890
Status: SUCCESS
Notification Type: VIDEO_CALL
===================================
```

### Error Log:
```
=== CALL NOTIFICATION FAILED ===
Error Code: invalid-registration-token
Error Message: The registration token is not valid
Messaging Error Code: INVALID_ARGUMENT
HTTP Status Code: 400
================================
```

## 🧪 Testing Checklist

- [ ] Service account key downloaded and placed in `src/main/resources/`
- [ ] Application restarted successfully
- [ ] No Firebase initialization errors in logs
- [ ] Test call notification with cURL
- [ ] Verify notification received on device
- [ ] Check notification appears in foreground
- [ ] Check notification appears in background
- [ ] Verify data payload is correct
- [ ] Test with both VIDEO_CALL and VOICE_CALL types

## 🔐 Security Notes

1. **Never commit** `service-account-key.json` to version control
2. The file is already in `.gitignore`
3. Rotate service account keys periodically
4. Use environment-specific keys for dev/staging/production
5. Restrict service account permissions to only FCM

## 📱 Client-Side Requirements

### Android App Must:
1. Create notification channels on app startup:
   - Channel ID: `calls` (for call notifications)
   - Channel ID: `messages` (for message notifications)
2. Request notification permissions
3. Handle FCM messages in foreground and background
4. Register FCM token with backend

### iOS App Must:
1. Request notification permissions
2. Configure APNS certificates in Firebase Console
3. Handle remote notifications
4. Register FCM token with backend

## 🚀 Production Recommendations

1. **Error Handling:** Already implemented with detailed logging
2. **Token Validation:** Consider storing and validating tokens in database
3. **Rate Limiting:** Add rate limiting to prevent notification spam
4. **Monitoring:** Set up alerts for FCM failures
5. **Fallback:** Implement alternative notification methods if FCM fails
6. **Token Refresh:** Implement endpoint to update device tokens
7. **Multi-Device:** Support multiple device tokens per user

## 📖 Additional Resources

- [Firebase Admin SDK Documentation](https://firebase.google.com/docs/admin/setup)
- [FCM HTTP v1 API](https://firebase.google.com/docs/cloud-messaging/http-server-ref)
- [Android FCM Client](https://firebase.google.com/docs/cloud-messaging/android/client)
- [iOS FCM Client](https://firebase.google.com/docs/cloud-messaging/ios/client)

## ✨ Summary

Your backend implementation is complete and production-ready! The only thing you need to do is:

1. Download the service account key from Firebase Console
2. Place it in `src/main/resources/service-account-key.json`
3. Restart your application
4. Test with the provided cURL command

Once configured, notifications will be delivered to devices immediately!
