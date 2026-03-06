# FCM Notification Troubleshooting Guide

## 🔍 Quick Diagnostic Checklist

### Backend Checks

1. **Service Account Key Exists**
   ```bash
   ls -la src/main/resources/service-account-key.json
   ```
   - Should show the file exists
   - File size should be > 2KB

2. **Application Starts Without Errors**
   - Check logs for: `Failed to initialize Firebase`
   - If error appears, service account key is missing or invalid

3. **Endpoint is Accessible**
   ```bash
   curl -X POST 'http://192.168.1.9:3000/api/notifications/call' \
     -H 'Content-Type: application/json' \
     -d '{"type":"VIDEO_CALL","device_token":"test","callerName":"Test","callerId":"1","roomName":"room","callId":"123"}'
   ```
   - Should return 200 or 500 (not 404)

### Common Issues and Solutions

## Issue 1: "Failed to initialize Firebase"

**Symptoms:**
- Application fails to start
- Error in logs: `Failed to initialize Firebase`

**Causes:**
1. Service account key file not found
2. Invalid JSON in service account key
3. Wrong file path

**Solutions:**
1. Verify file exists: `src/main/resources/service-account-key.json`
2. Validate JSON: `cat src/main/resources/service-account-key.json | python -m json.tool`
3. Check file permissions: `ls -la src/main/resources/service-account-key.json`
4. Re-download from Firebase Console if corrupted

## Issue 2: "Invalid registration token"

**Symptoms:**
- HTTP 200 response but error in logs
- Error: `The registration token is not valid`

**Causes:**
1. Device token expired
2. Token from different Firebase project
3. Token format incorrect
4. App uninstalled/reinstalled

**Solutions:**
1. Get fresh token from device (user must log in again)
2. Verify Firebase project matches between app and backend
3. Check token format: Should be ~150+ characters
4. Test with a known working token

## Issue 3: Notification Sent but Not Received

**Symptoms:**
- Backend logs show success
- Firebase returns message ID
- Device doesn't receive notification

**Causes:**
1. Device has no internet
2. App doesn't have notification permissions
3. Battery optimization killing app
4. Notification channel not created (Android)
5. App in background and not handling data-only messages

**Solutions:**

### Android:
1. Check notification permissions:
   - Settings → Apps → Your App → Notifications → Enabled
2. Disable battery optimization:
   - Settings → Battery → Battery Optimization → Your App → Don't optimize
3. Verify notification channels created:
   ```java
   // In your Android app
   NotificationChannel channel = new NotificationChannel(
       "calls",
       "Call Notifications",
       NotificationManager.IMPORTANCE_HIGH
   );
   channel.setSound(defaultSoundUri, audioAttributes);
   notificationManager.createNotificationChannel(channel);
   ```
4. Check FCM service is running:
   ```java
   // Verify FirebaseMessaging is initialized
   FirebaseMessaging.getInstance().getToken()
       .addOnCompleteListener(task -> {
           if (task.isSuccessful()) {
               String token = task.getResult();
               Log.d("FCM", "Token: " + token);
           }
       });
   ```

### iOS:
1. Check notification permissions granted
2. Verify APNS certificates configured in Firebase Console
3. Check app is registered for remote notifications
4. Verify provisioning profile includes push notifications

## Issue 4: Wrong Notification Type

**Symptoms:**
- Notification shows wrong title
- Expected "Incoming Video Call" but shows "Incoming Voice Call"

**Cause:**
- Type field case mismatch

**Solution:**
- Backend now handles case-insensitive types
- Use either: `VIDEO_CALL`, `video_call`, `VOICE_CALL`, `voice_call`

## Issue 5: Data Not Passed to App

**Symptoms:**
- Notification received but data payload empty
- App can't extract caller info

**Cause:**
- App not reading data payload correctly

**Solution:**

### Android:
```java
@Override
public void onMessageReceived(RemoteMessage remoteMessage) {
    // Get data payload
    Map<String, String> data = remoteMessage.getData();
    
    String type = data.get("type");
    String callerName = data.get("callerName");
    String callerId = data.get("callerId");
    String roomName = data.get("roomName");
    String callId = data.get("callId");
    
    Log.d("FCM", "Type: " + type);
    Log.d("FCM", "Caller: " + callerName);
    
    // Handle the call...
}
```

### iOS:
```swift
func userNotificationCenter(_ center: UNUserNotificationCenter,
                          didReceive response: UNNotificationResponse,
                          withCompletionHandler completionHandler: @escaping () -> Void) {
    let userInfo = response.notification.request.content.userInfo
    
    if let type = userInfo["type"] as? String,
       let callerName = userInfo["callerName"] as? String,
       let callerId = userInfo["callerId"] as? String,
       let roomName = userInfo["roomName"] as? String,
       let callId = userInfo["callId"] as? String {
        
        print("Type: \(type)")
        print("Caller: \(callerName)")
        
        // Handle the call...
    }
    
    completionHandler()
}
```

## Testing Steps

### Step 1: Verify Backend Configuration

1. Check service account key exists:
   ```bash
   ls -la src/main/resources/service-account-key.json
   ```

2. Start application and check logs:
   ```bash
   ./gradlew bootRun
   ```
   - Look for: "Firebase initialized successfully" (or no errors)

### Step 2: Get Device Token

From your Android app logs:
```
FCM Token: eUUu8zqzTpCRGP9nUPqBzf:APA91bF6mn_6VkTkUF4uWewhY3U...
```

### Step 3: Send Test Notification

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

### Step 4: Check Backend Logs

Look for:
```
=== CALL NOTIFICATION RESPONSE ===
Firebase Response: projects/your-project/messages/0:1234567890
Status: SUCCESS
===================================
```

### Step 5: Check Device

- Notification should appear within 1-2 seconds
- If not, check device logs for FCM messages

## Debugging Commands

### Check if endpoint is accessible:
```bash
curl -v http://192.168.1.9:3000/api/notifications/call
```

### Test with minimal payload:
```bash
curl -X POST 'http://192.168.1.9:3000/api/notifications/call' \
  -H 'Content-Type: application/json' \
  -d '{
    "type": "VIDEO_CALL",
    "device_token": "test_token",
    "callerName": "Test",
    "callerId": "1",
    "roomName": "room",
    "callId": "123"
  }'
```

### Check application logs:
```bash
tail -f logs/spring-boot-application.log
```

### Verify Firebase project ID:
```bash
cat src/main/resources/service-account-key.json | grep project_id
```

## Firebase Console Checks

1. **Verify Project**
   - Go to Firebase Console
   - Check you're in the correct project
   - Project ID should match service account key

2. **Check Cloud Messaging**
   - Firebase Console → Cloud Messaging
   - Verify API is enabled

3. **Test from Console**
   - Firebase Console → Cloud Messaging → Send test message
   - Enter device token
   - Send notification
   - If this works, backend configuration is the issue
   - If this doesn't work, device/app configuration is the issue

## Network Issues

### Check Backend is Reachable:
```bash
ping 192.168.1.9
```

### Check Port is Open:
```bash
telnet 192.168.1.9 3000
```

### Check Firewall:
```bash
# On backend server
sudo ufw status
sudo ufw allow 3000/tcp
```

## Production Checklist

- [ ] Service account key from production Firebase project
- [ ] Environment-specific configuration
- [ ] HTTPS enabled (not HTTP)
- [ ] Rate limiting configured
- [ ] Error monitoring set up
- [ ] Token refresh mechanism implemented
- [ ] Notification delivery tracking
- [ ] Fallback notification method
- [ ] Load testing completed
- [ ] Security audit performed

## Getting Help

If issues persist:

1. **Check Backend Logs**
   - Look for detailed error messages
   - Note the error code and message

2. **Check Device Logs**
   - Android: `adb logcat | grep FCM`
   - iOS: Xcode console

3. **Verify Configuration**
   - Firebase project matches
   - Service account key is valid
   - Device token is current

4. **Test Isolation**
   - Test from Firebase Console directly
   - Test with different device
   - Test with different token

5. **Common Error Codes**
   - `invalid-registration-token`: Token expired or invalid
   - `registration-token-not-registered`: Token deleted or app uninstalled
   - `invalid-argument`: Malformed request
   - `sender-id-mismatch`: Token from different project
   - `quota-exceeded`: Too many requests

## Contact Information

For Firebase-specific issues:
- [Firebase Support](https://firebase.google.com/support)
- [Stack Overflow - Firebase](https://stackoverflow.com/questions/tagged/firebase)

For backend issues:
- Check application logs
- Review `NotificationService.java` implementation
- Verify `FirebaseConfig.java` initialization
