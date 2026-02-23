# JWT Authentication Implementation Guide

## Overview
JWT (JSON Web Token) authentication has been successfully integrated into the Mindaro Backend application. Users now receive a JWT token upon login/registration, which is stored in the database and can be used for subsequent authenticated requests.

## What's New

### 1. JWT Token Generation
- JWT tokens are automatically generated on login and registration
- Tokens are valid for 30 days
- Tokens include user email and user ID as claims

### 2. Database Changes
- Added `jwt_token` column to `a1_user` table to store user tokens

### 3. New API Endpoint

#### POST `/api/user/token-signin`
Authenticate using a JWT token instead of email/password.

**Request Body:**
```json
{
  "jwt_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Response:**
```json
{
  "id": "uuid",
  "name": "User Name",
  "email": "user@example.com",
  "mobile": "9876543210",
  "country": "IN",
  "user_type": "CUSTOMER",
  "is_profile_completed": true,
  "jwt_token": "new_refreshed_token..."
}
```

### 4. Updated Existing Endpoints

#### POST `/api/user/login`
Now returns JWT token in response.

**Response includes:**
```json
{
  "id": "uuid",
  "name": "User Name",
  "email": "user@example.com",
  "mobile": "9876543210",
  "country": "IN",
  "user_type": "CUSTOMER",
  "is_profile_completed": true,
  "jwt_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

#### POST `/api/user/register`
Now returns JWT token in response with the same structure as login.

## How to Use

### 1. Login/Register
```bash
curl -X POST http://localhost:3000/api/user/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "password123",
    "method": "password"
  }'
```

Save the `jwt_token` from the response.

### 2. Token-Based Sign In
```bash
curl -X POST http://localhost:3000/api/user/token-signin \
  -H "Content-Type: application/json" \
  -d '{
    "jwt_token": "your_saved_token_here"
  }'
```

### 3. Authenticated Requests
For protected endpoints, include the JWT token in the Authorization header:

```bash
curl -X GET http://localhost:3000/api/user/user@example.com \
  -H "Authorization: Bearer your_jwt_token_here"
```

## Security Configuration

### Public Endpoints (No Authentication Required)
- `/api/user/login`
- `/api/user/register`
- `/api/user/token-signin`
- `/swagger-ui/**`
- `/v3/api-docs/**`

### Protected Endpoints
All other endpoints require a valid JWT token in the Authorization header.

## Technical Details

### Components Added

1. **JwtUtil** (`security/JwtUtil.java`)
   - Generates JWT tokens
   - Validates tokens
   - Extracts claims (email, userId)

2. **JwtAuthenticationFilter** (`security/JwtAuthenticationFilter.java`)
   - Intercepts requests
   - Validates JWT tokens from Authorization header
   - Sets authentication context

3. **CustomUserDetailsService** (`security/CustomUserDetailsService.java`)
   - Loads user details for Spring Security

4. **SecurityConfig** (`config/SecurityConfig.java`)
   - Configures Spring Security
   - Defines public/protected endpoints
   - Integrates JWT filter

5. **TokenSignInRequest** (`payload/request/TokenSignInRequest.java`)
   - DTO for token-based sign-in

### Dependencies Added
```gradle
implementation 'io.jsonwebtoken:jjwt-api:0.12.3'
runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.12.3'
runtimeOnly 'io.jsonwebtoken:jjwt-jackson:0.12.3'
```

## Token Structure

JWT tokens contain:
- **Subject**: User email
- **userId**: User UUID
- **Issued At**: Token creation timestamp
- **Expiration**: 30 days from creation

## Next Steps

1. Run `./gradlew build` to download new dependencies
2. Restart the application
3. Test the new endpoints using Swagger UI at `http://localhost:3000/swagger-ui.html`

## Notes

- Tokens are automatically refreshed on token-based sign-in
- The original `application.properties` file remains unchanged
- Existing authentication methods (password, Google) continue to work
- JWT secret key is hardcoded for simplicity (consider using environment variables in production)
