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

    public UserDto updateUserProfile(UserRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (RegexUtils.isValidEmail(request.getEmail())) {
            user.setEmail(request.getEmail());
        }
        if (RegexUtils.isValidUsername(request.getName())) {
            user.setName(request.getName());
        }
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPwd(request.getPassword());
            user.setPwd(passwordEncoder.encode(request.getPassword()));
        }

        userRepository.save(user);
        return ObjectMapperUtils.map(user, UserDto.class);
    }

    public boolean isEmailRegistered(String email) {
        return userRepository.existsByEmail(email);
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
