package com.dekhokaun.mindarobackend.service;

import com.dekhokaun.mindarobackend.exception.InvalidAuthException;
import com.dekhokaun.mindarobackend.exception.InvalidRequestException;
import com.dekhokaun.mindarobackend.model.User;
import com.dekhokaun.mindarobackend.model.UserType;
import com.dekhokaun.mindarobackend.payload.request.UpdateUserRequest;
import com.dekhokaun.mindarobackend.payload.request.UserRequest;
import com.dekhokaun.mindarobackend.payload.response.UserResponse;
import com.dekhokaun.mindarobackend.repository.UserRepository;
import com.dekhokaun.mindarobackend.security.JwtUtil;
import com.dekhokaun.mindarobackend.utils.RegexUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public UserResponse authenticateOrRegister(UserRequest request) {
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());

        if (existingUser.isPresent()) {
            // User Exists: Verify via Google or Password
            User user = existingUser.get();

            if ("google".equalsIgnoreCase(request.getMethod())) {
                if (!request.getToken().equals(user.getToken())) {
                    throw new InvalidAuthException("Invalid Google authentication token");
                }
            } else {
                if (!passwordEncoder.matches(request.getPassword(), user.getPwd())) {
                    throw new InvalidRequestException("Invalid password");
                }
            }

            // Generate and save JWT token
            String jwtToken = jwtUtil.generateToken(user.getEmail(), user.getId());
            user.setJwtToken(jwtToken);
            userRepository.save(user);

            return mapToUserResponse(user);
        }

        // User Doesn't Exist: Create New User
        User newUser = new User();
        newUser.setEmail(request.getEmail());
        newUser.setName(request.getName());
        newUser.setPwd(passwordEncoder.encode(request.getPassword()));
        newUser.setCountry(request.getCountry());

        String mobile = request.getMobile();
        if (mobile != null && !mobile.isEmpty() && RegexUtils.isValidPhoneNumber(request.getMobile(), "IN")) {
            newUser.setMobile(Long.valueOf(request.getMobile()));
        }

        User savedUser = userRepository.save(newUser);

        // Generate and save JWT token
        String jwtToken = jwtUtil.generateToken(savedUser.getEmail(), savedUser.getId());
        savedUser.setJwtToken(jwtToken);
        userRepository.save(savedUser);

        return mapToUserResponse(savedUser);
    }
    @Transactional
    public UserResponse login(String email, String password, String method, String token) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidAuthException("User not found with this email"));

        if ("google".equalsIgnoreCase(method)) {
            if (token == null || !token.equals(user.getToken())) {
                throw new InvalidAuthException("Invalid Google authentication token");
            }
        } else {
            if (password == null || !passwordEncoder.matches(password, user.getPwd())) {
                throw new InvalidAuthException("Invalid password");
            }
        }

        // Generate and save JWT token
        String jwtToken = jwtUtil.generateToken(user.getEmail(), user.getId());
        user.setJwtToken(jwtToken);
        userRepository.save(user);

        return mapToUserResponse(user);
    }

    @Transactional
    public UserResponse tokenSignIn(String jwtToken) {
        try {
            String email = jwtUtil.extractEmail(jwtToken);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new InvalidAuthException("User not found"));

            if (!jwtUtil.validateToken(jwtToken, email)) {
                throw new InvalidAuthException("Invalid or expired token");
            }

            // Optionally refresh the token
            String newJwtToken = jwtUtil.generateToken(user.getEmail(), user.getId());
            user.setJwtToken(newJwtToken);
            userRepository.save(user);

            return mapToUserResponse(user);
        } catch (Exception e) {
            throw new InvalidAuthException("Invalid token: " + e.getMessage());
        }
    }

    public UserResponse getUserProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidRequestException("User not found"));

        return mapToUserResponse(user);
    }

    public UserResponse updateUserProfile(UpdateUserRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidRequestException("User not found"));

        /*
           if (request.getEmail() != null && RegexUtils.isValidEmail(request.getEmail())) {
            user.setEmail(request.getEmail());
           }
        */

        if (request.getName() != null && RegexUtils.isValidUsername(request.getName())) {
            user.setName(request.getName());
        }

        /* if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPwd(passwordEncoder.encode(request.getPassword()));
        } */

        if (request.getMobile() != null && RegexUtils.isValidPhoneNumber(request.getMobile(), "IN")) {
            user.setMobile(Long.valueOf(request.getMobile()));
        }

        if (request.getCountry() != null) {
            user.setCountry(request.getCountry());
        }
        user.setIsProfileCompleted(true);

        userRepository.save(user);
        return mapToUserResponse(user);
    }

    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidRequestException("User not found with email: " + email));

        return mapToUserResponse(user);
    }

    public void deleteUser(String id) {
        if (!userRepository.existsById(UUID.fromString(id))) {
            throw new InvalidRequestException("User not found with id: " + id);
        }
        userRepository.deleteById(UUID.fromString(id));
    }

    private UserResponse mapToUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                String.valueOf(user.getMobile()),
                user.getCountry(),
                user.getUtype().toString(),
                user.getIsProfileCompleted(),
                user.getJwtToken()
        );
    }

    public void createTestUser() {
        User testUser = new User();
        testUser.setName("Test User");
        testUser.setEmail("testuser@example.com");
        testUser.setMobile(9876543210L);
        testUser.setCountry("IN");
        testUser.setPwd(passwordEncoder.encode("test123"));
        testUser.setUtype(UserType.CUSTOMER);
        testUser.setIsProfileCompleted(false);
        testUser.setStatus("active");
        testUser.setToken("test_token_123");
        testUser.setHowtoknow("Through Testing");
        testUser.setRating(BigDecimal.valueOf(4.5));
        testUser.setRatingcount(10);

        userRepository.save(testUser);
    }

    @Transactional
    public void updateDeviceToken(String userId, String deviceToken) {
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new InvalidRequestException("User not found with id: " + userId));

        user.setDeviceToken(deviceToken);
        userRepository.save(user);
    }



}
