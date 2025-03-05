package com.dekhokaun.mindarobackend.service;

import com.dekhokaun.mindarobackend.dto.UserDto;
import com.dekhokaun.mindarobackend.model.User;
import com.dekhokaun.mindarobackend.payload.request.UserRequest;
import com.dekhokaun.mindarobackend.payload.response.UserResponse;
import com.dekhokaun.mindarobackend.repository.UserRepository;
import com.dekhokaun.mindarobackend.utils.ObjectMapperUtils;
import com.dekhokaun.mindarobackend.utils.RegexUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;

    // PasswordEncoder passwordEncoder use this as password encoder in user service params
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
    }

    public UserDto registerUser(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        // passwordEncoder.encode(request.getPassword())
        user.setPwd(request.getPassword());
        userRepository.save(user);

        return ObjectMapperUtils.map(user, UserDto.class);
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
//            user.setPwd(passwordEncoder.encode(request.getPassword()));
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
}
