package com.dekhokaun.mindarobackend.service;

import com.dekhokaun.mindarobackend.exception.InvalidRequestException;
import com.dekhokaun.mindarobackend.model.User;
import com.dekhokaun.mindarobackend.model.UserType;
import com.dekhokaun.mindarobackend.payload.request.CreateAdminRequest;
import com.dekhokaun.mindarobackend.payload.response.UserResponse;
import com.dekhokaun.mindarobackend.repository.UserRepository;
import com.dekhokaun.mindarobackend.utils.RegexUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public UserResponse createAdmin(CreateAdminRequest request, String creatorEmail) {
        // Verify that creator is an admin
        User creator = userRepository.findByEmail(creatorEmail)
                .orElseThrow(() -> new InvalidRequestException("Creator not found"));
        
        if (creator.getUtype() != UserType.ADMIN) {
            throw new InvalidRequestException("Only admins can create other admins");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new InvalidRequestException("Email already exists");
        }

        // Create new admin user
        User newAdmin = new User();
        newAdmin.setEmail(request.getEmail());
        newAdmin.setName(request.getName());
        newAdmin.setPwd(passwordEncoder.encode(request.getPassword()));
        newAdmin.setCountry(request.getCountry());
        newAdmin.setUtype(UserType.ADMIN);
        newAdmin.setIsProfileCompleted(true);

        String mobile = request.getMobile();
        if (mobile != null && !mobile.isEmpty() && RegexUtils.isValidPhoneNumber(mobile, "IN")) {
            newAdmin.setMobile(Long.valueOf(mobile));
        }

        userRepository.save(newAdmin);
        return mapToUserResponse(newAdmin);
    }

    public List<UserResponse> getAllUsers(String adminEmail) {
        // Verify that requester is an admin
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new InvalidRequestException("Admin not found"));
        
        if (admin.getUtype() != UserType.ADMIN) {
            throw new InvalidRequestException("Only admins can view all users");
        }

        return userRepository.findAll().stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteUser(String userEmail, String adminEmail) {
        // Verify that requester is an admin
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new InvalidRequestException("Admin not found"));
        
        if (admin.getUtype() != UserType.ADMIN) {
            throw new InvalidRequestException("Only admins can delete users");
        }

        User userToDelete = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new InvalidRequestException("User not found"));

        // Prevent deleting another admin
        if (userToDelete.getUtype() == UserType.ADMIN && !adminEmail.equals(userEmail)) {
            throw new InvalidRequestException("Admins cannot delete other admins");
        }

        userRepository.delete(userToDelete);
    }

    private UserResponse mapToUserResponse(User user) {
        return new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            String.valueOf(user.getMobile()),
            user.getCountry(),
            user.getUtype().toString(),
            user.getIsProfileCompleted()
        );
    }
} 