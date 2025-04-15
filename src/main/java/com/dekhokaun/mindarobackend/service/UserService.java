package com.dekhokaun.mindarobackend.service;

import com.dekhokaun.mindarobackend.exception.InvalidAuthException;
import com.dekhokaun.mindarobackend.exception.InvalidRequestException;
import com.dekhokaun.mindarobackend.model.User;
import com.dekhokaun.mindarobackend.payload.request.CreateUserRequest;
import com.dekhokaun.mindarobackend.payload.request.UpdateUserRequest;
import com.dekhokaun.mindarobackend.payload.response.UserResponse;
import com.dekhokaun.mindarobackend.repository.UserRepository;
import com.dekhokaun.mindarobackend.utils.RegexUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public UserResponse registerUser(CreateUserRequest request) {
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());

        if (existingUser.isPresent()) {
            throw new InvalidRequestException("User already exists. Please log in.");
        }

        User newUser = new User();
        newUser.setEmail(request.getEmail());
        newUser.setPwd(passwordEncoder.encode(request.getPassword()));


        userRepository.save(newUser);
        return mapToUserResponse(newUser);
    }

    public UserResponse loginUser(CreateUserRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidAuthException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPwd())) {
            throw new InvalidAuthException("Invalid credentials");
        }

        return mapToUserResponse(user);
    }

    public UserResponse updateUserProfile(UpdateUserRequest request) {
        if (request.getEmail() == null && request.getMobile() == null) {
            throw new InvalidRequestException("Email or Mobile must be provided");
        }

        User user = userRepository.findByEmailOrMobile(request.getEmail(),
                        request.getMobile() != null ? Long.valueOf(request.getMobile()) : null)
                .orElseThrow(() -> new InvalidRequestException("User not found"));

        if (request.getName() != null && RegexUtils.isValidUsername(request.getName())) {
            user.setName(request.getName());
        } else {
            throw new InvalidRequestException("Username should not include any special characters");
        }
//        if (request.getPassword() != null && !request.getPassword().isBlank()) {
//            user.setPwd(passwordEncoder.encode(request.getPassword()));
//        }

        if (request.getMobile() != null && RegexUtils.isValidPhoneNumber(request.getMobile(), "IN")) {
            user.setMobile(Long.valueOf(request.getMobile()));
        } else {
            throw new InvalidRequestException("Use a valid phone number");
        }

        if (request.getCountry() != null) {
            user.setCountry(request.getCountry());
        }

        user.setProfileCompleted(true);
        userRepository.save(user);
        return mapToUserResponse(user);
    }

    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidRequestException("User not found with email: " + email));

        return mapToUserResponse(user);
    }

    private UserResponse mapToUserResponse(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), String.valueOf(user.getMobile()), user.getCountry(), user.getUtype().toString(), user.isProfileCompleted(), user.getCreatedAt());
    }

}
