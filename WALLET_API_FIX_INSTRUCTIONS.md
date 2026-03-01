# Wallet API Authentication Fix

## Problem
The wallet transactions endpoint is failing because the frontend is not sending the JWT token in the Authorization header.

## Backend Changes Made
1. Made `userId` optional in `WalletAddMoneyRequest`
2. Updated wallet endpoints to support two authentication methods:
   - JWT token in Authorization header (recommended)
   - userId in request body (fallback)
3. Added detailed logging to help debug authentication issues

## Frontend Fix Required

The frontend needs to send the JWT token in the Authorization header when calling wallet endpoints.

### Current Frontend Code (BROKEN)
```typescript
// WalletApiService.ts - getTransactions method
const response = await fetch(`${this.baseUrl}/api/wallet/transactions`, {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({}), // Empty body - no userId!
});
```

### Fixed Frontend Code (OPTION 1 - Recommended)
```typescript
// WalletApiService.ts - getTransactions method
const jwtToken = await AsyncStorage.getItem('jwtToken');

const response = await fetch(`${this.baseUrl}/api/wallet/transactions`, {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${jwtToken}`, // ADD THIS LINE
  },
  body: JSON.stringify({}), // Body can be empty when JWT is sent
});
```

### Fixed Frontend Code (OPTION 2 - Fallback)
```typescript
// WalletApiService.ts - getTransactions method
const userId = await AsyncStorage.getItem('userId'); // Get stored userId

const response = await fetch(`${this.baseUrl}/api/wallet/transactions`, {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({ userId }), // Include userId in body
});
```

## API Endpoints

### POST /api/wallet/transactions
**Description:** Get wallet transaction history

**Authentication:** One of the following is required:
- JWT token in `Authorization: Bearer <token>` header (recommended)
- `userId` in request body

**Request Body (if not using JWT):**
```json
{
  "userId": "ef66fcd4-b3a1-4332-a3cf-74cdf7d32713"
}
```

**Request Headers (if using JWT):**
```
Authorization: Bearer eyJhbGciOiJIUzM4NCJ9...
Content-Type: application/json
```

**Response:**
```json
{
  "userId": "ef66fcd4-b3a1-4332-a3cf-74cdf7d32713",
  "transactions": [
    {
      "id": "...",
      "amount": 100.00,
      "type": "CREDIT",
      "timestamp": "2026-03-01T09:30:00"
    }
  ]
}
```

### POST /api/wallet/add-money
**Description:** Add money to wallet

**Authentication:** Same as transactions endpoint

**Request Body:**
```json
{
  "userId": "ef66fcd4-b3a1-4332-a3cf-74cdf7d32713",  // Optional if JWT is sent
  "amount": 100.00,
  "method": "payment"
}
```

## Testing

### Test with curl (using JWT):
```bash
curl -X POST http://10.42.208.115:3000/api/wallet/transactions \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE" \
  -d '{}'
```

### Test with curl (using userId):
```bash
curl -X POST http://10.42.208.115:3000/api/wallet/transactions \
  -H "Content-Type: application/json" \
  -d '{"userId": "ef66fcd4-b3a1-4332-a3cf-74cdf7d32713"}'
```

## Next Steps
1. Update the frontend WalletApiService to include the Authorization header
2. Ensure the JWT token is being retrieved from storage before making wallet API calls
3. Test the wallet endpoints with the updated frontend code
