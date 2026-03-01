# Wallet API - Updated Documentation

## Changes Made

### 1. Transaction Type Enum
Created `TransactionType` enum with two values:
- `CREDIT` - Money added to wallet
- `DEBIT` - Money deducted from wallet

### 2. Database Schema Updates
Updated `WalletTransaction` model:
- `transactionType` is now an enum (was String)
- Added proper column constraints and lengths
- `createdAt` field already existed and is included in responses

### 3. API Response Updates
`WalletTransactionResponse` now includes:
- `id` - Transaction UUID
- `walletId` - Associated wallet UUID
- `transactionType` - "CREDIT" or "DEBIT"
- `amount` - Transaction amount
- `status` - Transaction status (PENDING, SUCCESS, FAILED)
- `paymentGatewayReference` - Payment method or reference
- `createdAt` - Timestamp when transaction was created

### 4. Transaction Ordering
Transactions are now returned in descending order by `createdAt` (newest first)

## API Endpoints

### GET /api/wallet/balance/{userId}
Get wallet balance for a user.

**Response:**
```json
{
  "userId": "ef66fcd4-b3a1-4332-a3cf-74cdf7d32713",
  "balance": 1500.00,
  "createdAt": "2026-03-01T09:00:00",
  "updatedAt": "2026-03-01T09:30:00"
}
```

### POST /api/wallet/add-money
Add money to wallet (CREDIT transaction).

**Authentication:** JWT token in Authorization header OR userId in request body

**Request:**
```json
{
  "userId": "ef66fcd4-b3a1-4332-a3cf-74cdf7d32713",  // Optional if JWT sent
  "amount": 500.00,
  "method": "razorpay"
}
```

**Response:**
```json
{
  "userId": "ef66fcd4-b3a1-4332-a3cf-74cdf7d32713",
  "balance": 2000.00,
  "createdAt": "2026-03-01T09:00:00",
  "updatedAt": "2026-03-01T09:35:00"
}
```

### POST /api/wallet/transactions
Get wallet transaction history.

**Authentication:** JWT token in Authorization header OR userId in request body

**Request (with JWT):**
```json
{}
```

**Request (without JWT):**
```json
{
  "userId": "ef66fcd4-b3a1-4332-a3cf-74cdf7d32713"
}
```

**Response:**
```json
{
  "userId": "ef66fcd4-b3a1-4332-a3cf-74cdf7d32713",
  "transactions": [
    {
      "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
      "walletId": "w1x2y3z4-a5b6-c7d8-e9f0-123456789abc",
      "transactionType": "CREDIT",
      "amount": 500.00,
      "status": "SUCCESS",
      "paymentGatewayReference": "razorpay",
      "createdAt": "2026-03-01T09:35:00"
    },
    {
      "id": "b2c3d4e5-f6g7-8901-bcde-f12345678901",
      "walletId": "w1x2y3z4-a5b6-c7d8-e9f0-123456789abc",
      "transactionType": "DEBIT",
      "amount": 100.00,
      "status": "SUCCESS",
      "paymentGatewayReference": "credit_purchase",
      "createdAt": "2026-03-01T09:30:00"
    },
    {
      "id": "c3d4e5f6-g7h8-9012-cdef-123456789012",
      "walletId": "w1x2y3z4-a5b6-c7d8-e9f0-123456789abc",
      "transactionType": "CREDIT",
      "amount": 1000.00,
      "status": "SUCCESS",
      "paymentGatewayReference": "paytm",
      "createdAt": "2026-03-01T09:00:00"
    }
  ]
}
```

## Transaction Types

### CREDIT Transactions
Money is added to the wallet. Examples:
- User adds money via payment gateway
- Refund received
- Promotional credit

### DEBIT Transactions
Money is deducted from the wallet. Examples:
- Purchasing credits
- Booking a session
- Service charges

## Frontend Integration

### Display Transaction Type
```typescript
const getTransactionIcon = (type: string) => {
  return type === 'CREDIT' ? '↑' : '↓';
};

const getTransactionColor = (type: string) => {
  return type === 'CREDIT' ? 'green' : 'red';
};

const getTransactionLabel = (type: string) => {
  return type === 'CREDIT' ? 'Money Added' : 'Money Deducted';
};
```

### Display Transaction List
```typescript
transactions.map(tx => (
  <View key={tx.id}>
    <Text style={{ color: getTransactionColor(tx.transactionType) }}>
      {getTransactionIcon(tx.transactionType)} 
      {getTransactionLabel(tx.transactionType)}
    </Text>
    <Text>₹{tx.amount}</Text>
    <Text>{new Date(tx.createdAt).toLocaleString()}</Text>
    <Text>{tx.status}</Text>
  </View>
))
```

## Database Migration

If you need to migrate existing data, run this SQL:

```sql
-- Update existing string values to enum values (if any exist)
UPDATE a1_wallet_transaction 
SET transaction_type = 'CREDIT' 
WHERE UPPER(transaction_type) = 'CREDIT';

UPDATE a1_wallet_transaction 
SET transaction_type = 'DEBIT' 
WHERE UPPER(transaction_type) = 'DEBIT';

-- Alter column to use enum (if needed)
ALTER TABLE a1_wallet_transaction 
MODIFY COLUMN transaction_type VARCHAR(10) NOT NULL;
```

## Testing

### Test CREDIT Transaction
```bash
curl -X POST http://localhost:3000/api/wallet/add-money \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "amount": 500.00,
    "method": "test_payment"
  }'
```

### Test Transaction History
```bash
curl -X POST http://localhost:3000/api/wallet/transactions \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{}'
```

## Error Handling

### Insufficient Balance (DEBIT)
When trying to deduct more than available balance:
```json
{
  "error": "InsufficientBalanceException",
  "message": "Insufficient balance in wallet."
}
```

### Invalid Amount
When amount is zero or negative:
```json
{
  "error": "InvalidTransactionException",
  "message": "Amount must be greater than zero."
}
```

### Authentication Error
When neither JWT nor userId is provided:
```json
{
  "error": "InvalidAuthException",
  "message": "Please provide userId in request body or send a valid JWT token in Authorization header"
}
```
