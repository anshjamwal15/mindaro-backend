package com.dekhokaun.mindarobackend.service;

import com.dekhokaun.mindarobackend.dto.UserDto;
import com.dekhokaun.mindarobackend.model.User;
import com.dekhokaun.mindarobackend.payload.request.UserRequest;
import com.dekhokaun.mindarobackend.payload.response.UserResponse;
import com.dekhokaun.mindarobackend.repository.UserRepository;
import com.dekhokaun.mindarobackend.utils.ObjectMapperUtils;
import com.dekhokaun.mindarobackend.utils.RegexUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponse authenticateOrRegister(UserRequest request) {
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());

        if (existingUser.isPresent()) {
            // User Exists: Verify via Google or Password
            User user = existingUser.get();

            if ("google".equalsIgnoreCase(request.getMethod())) {
                if (!request.getToken().equals(user.getToken())) {
                    throw new RuntimeException("Invalid Google authentication token");
                }
            } else {
                if (!passwordEncoder.matches(request.getPassword(), user.getPwd())) {
                    throw new RuntimeException("Invalid password");
                }
            }
            return mapToUserResponse(user);
        }

        // User Doesn't Exist: Create New User
        User newUser = new User();
        newUser.setEmail(request.getEmail());
        newUser.setName(request.getName());
        newUser.setToken(request.getMethod().equalsIgnoreCase("google") ? request.getToken() : null);
        newUser.setPwd(passwordEncoder.encode(request.getPassword()));
        userRepository.save(newUser);

        return mapToUserResponse(newUser);
    }

    public UserDto getUserProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ObjectMapperUtils.map(user, UserDto.class);
    }

    public UserResponse updateUserProfile(UserRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getEmail() != null && RegexUtils.isValidEmail(request.getEmail())) {
            user.setEmail(request.getEmail());
        }

        if (request.getName() != null && RegexUtils.isValidUsername(request.getName())) {
            user.setName(request.getName());
        }

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPwd(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getMobile() != null && RegexUtils.isValidIndianMobile(request.getMobile())) {
            user.setMobile(Long.valueOf(request.getMobile()));
        }

        if (request.getCountry() != null) {
            user.setCountry(request.getCountry());
        }
        userRepository.save(user);
        return mapToUserResponse(user);
    }

    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        return new UserResponse(user.getName(), user.getEmail(), user.getMobile().toString(), user.getCountry(), user.getUtype());
    }

    private UserResponse mapToUserResponse(User user) {
        return new UserResponse(user.getName(), user.getEmail(), String.valueOf(user.getMobile()), user.getCountry(), user.getUtype());
    }

}
