# JWT Authentication - Implementation Summary

## Files Created

### Security Layer
1. `src/main/java/com/dekhokaun/mindarobackend/security/JwtUtil.java`
   - JWT token generation and validation
   - Token expiry: 30 days
   - Extracts email and userId from tokens

2. `src/main/java/com/dekhokaun/mindarobackend/security/JwtAuthenticationFilter.java`
   - Intercepts HTTP requests
   - Validates Bearer tokens from Authorization header
   - Sets Spring Security context

3. `src/main/java/com/dekhokaun/mindarobackend/security/CustomUserDetailsService.java`
   - Implements UserDetailsService for Spring Security
   - Loads user by email

### Configuration
4. `src/main/java/com/dekhokaun/mindarobackend/config/SecurityConfig.java`
   - Spring Security configuration
   - Public endpoints: login, register, token-signin, swagger
   - Protected endpoints: all others require JWT
   - Stateless session management

### Request DTOs
5. `src/main/java/com/dekhokaun/mindarobackend/payload/request/TokenSignInRequest.java`
   - Request body for token-based authentication

## Files Modified

### Model
1. `src/main/java/com/dekhokaun/mindarobackend/model/User.java`
   - Added `jwtToken` field (TEXT column)

### Service
2. `src/main/java/com/dekhokaun/mindarobackend/service/UserService.java`
   - Injected JwtUtil and PasswordEncoder
   - Updated `login()` to generate and save JWT token
   - Updated `authenticateOrRegister()` to generate and save JWT token
   - Added `tokenSignIn()` method for token-based authentication

### Controller
3. `src/main/java/com/dekhokaun/mindarobackend/controller/UserController.java`
   - Added `/api/user/token-signin` endpoint
   - Imported TokenSignInRequest

### Response DTOs
4. `src/main/java/com/dekhokaun/mindarobackend/payload/response/UserResponse.java`
   - Added `jwtToken` field to response

### Build Configuration
5. `build.gradle`
   - Added JWT dependencies (jjwt-api, jjwt-impl, jjwt-jackson)

## Key Features

✅ JWT tokens generated on login/register  
✅ Tokens stored in database (`jwt_token` column)  
✅ New `/api/user/token-signin` endpoint for token-based auth  
✅ Token validation on protected endpoints  
✅ 30-day token expiry  
✅ Automatic token refresh on token-signin  
✅ Backward compatible with existing authentication  
✅ application.properties unchanged  

## API Endpoints

### Public (No Auth Required)
- POST `/api/user/login` - Returns JWT token
- POST `/api/user/register` - Returns JWT token
- POST `/api/user/token-signin` - Validates and refreshes JWT token

### Protected (JWT Required)
- GET `/api/user/{email}`
- PUT `/api/user/update`
- DELETE `/api/user/{id}`
- All other endpoints

## Usage Example

```bash
# 1. Login
curl -X POST http://localhost:3000/api/user/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"pass123","method":"password"}'

# Response includes jwt_token

# 2. Use token for authentication
curl -X GET http://localhost:3000/api/user/user@example.com \
  -H "Authorization: Bearer <jwt_token>"

# 3. Token-based sign in
curl -X POST http://localhost:3000/api/user/token-signin \
  -H "Content-Type: application/json" \
  -d '{"jwt_token":"<your_token>"}'
```

## Next Steps

1. Build the project: `./gradlew build`
2. Restart the application
3. Test endpoints via Swagger UI: `http://localhost:3000/swagger-ui.html`
4. Verify JWT token in database after login
