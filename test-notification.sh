#!/bin/bash

# FCM Notification Test Script
# Usage: ./test-notification.sh <device_token>

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Backend URL
BACKEND_URL="http://192.168.1.9:3000"

# Check if device token is provided
if [ -z "$1" ]; then
    echo -e "${RED}Error: Device token is required${NC}"
    echo "Usage: ./test-notification.sh <device_token>"
    echo ""
    echo "Example:"
    echo "./test-notification.sh eUUu8zqzTpCRGP9nUPqBzf:APA91bF6mn_6VkTkUF4uWewhY3U..."
    exit 1
fi

DEVICE_TOKEN=$1

echo -e "${YELLOW}=== FCM Notification Test ===${NC}"
echo ""

# Test 1: Video Call Notification
echo -e "${YELLOW}Test 1: Sending VIDEO_CALL notification...${NC}"
RESPONSE=$(curl -s -w "\n%{http_code}" -X POST "${BACKEND_URL}/api/notifications/call" \
  -H 'Content-Type: application/json' \
  -d "{
    \"type\": \"VIDEO_CALL\",
    \"device_token\": \"${DEVICE_TOKEN}\",
    \"callerName\": \"Test Caller\",
    \"callerId\": \"test_123\",
    \"roomName\": \"test_room_video\",
    \"callId\": \"test_call_video_$(date +%s)\"
  }")

HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | head -n-1)

if [ "$HTTP_CODE" -eq 200 ]; then
    echo -e "${GREEN}✓ Video call notification sent successfully${NC}"
    echo "Response: $BODY"
else
    echo -e "${RED}✗ Failed to send video call notification${NC}"
    echo "HTTP Code: $HTTP_CODE"
    echo "Response: $BODY"
fi

echo ""
sleep 2

# Test 2: Voice Call Notification
echo -e "${YELLOW}Test 2: Sending VOICE_CALL notification...${NC}"
RESPONSE=$(curl -s -w "\n%{http_code}" -X POST "${BACKEND_URL}/api/notifications/call" \
  -H 'Content-Type: application/json' \
  -d "{
    \"type\": \"VOICE_CALL\",
    \"device_token\": \"${DEVICE_TOKEN}\",
    \"callerName\": \"Test Caller\",
    \"callerId\": \"test_456\",
    \"roomName\": \"test_room_voice\",
    \"callId\": \"test_call_voice_$(date +%s)\"
  }")

HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | head -n-1)

if [ "$HTTP_CODE" -eq 200 ]; then
    echo -e "${GREEN}✓ Voice call notification sent successfully${NC}"
    echo "Response: $BODY"
else
    echo -e "${RED}✗ Failed to send voice call notification${NC}"
    echo "HTTP Code: $HTTP_CODE"
    echo "Response: $BODY"
fi

echo ""
sleep 2

# Test 3: Message Notification
echo -e "${YELLOW}Test 3: Sending MESSAGE notification...${NC}"
RESPONSE=$(curl -s -w "\n%{http_code}" -X POST "${BACKEND_URL}/api/notifications/message" \
  -H 'Content-Type: application/json' \
  -d "{
    \"device_token\": \"${DEVICE_TOKEN}\",
    \"senderName\": \"Test Sender\",
    \"senderId\": \"test_789\",
    \"message\": \"Hello! This is a test message.\",
    \"chatRoomId\": \"test_chat_room_$(date +%s)\"
  }")

HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | head -n-1)

if [ "$HTTP_CODE" -eq 200 ]; then
    echo -e "${GREEN}✓ Message notification sent successfully${NC}"
    echo "Response: $BODY"
else
    echo -e "${RED}✗ Failed to send message notification${NC}"
    echo "HTTP Code: $HTTP_CODE"
    echo "Response: $BODY"
fi

echo ""
echo -e "${YELLOW}=== Test Complete ===${NC}"
echo ""
echo "Check your device for notifications!"
echo "If you didn't receive notifications, check:"
echo "1. Backend logs for errors"
echo "2. Device has internet connection"
echo "3. App has notification permissions"
echo "4. Device token is valid and not expired"
