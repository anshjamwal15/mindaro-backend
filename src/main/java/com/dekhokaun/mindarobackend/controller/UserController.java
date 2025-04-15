package com.dekhokaun.mindarobackend.controller;

import com.dekhokaun.mindarobackend.payload.request.CreateUserRequest;
import com.dekhokaun.mindarobackend.payload.request.UpdateUserRequest;
import com.dekhokaun.mindarobackend.payload.response.UserResponse;
import com.dekhokaun.mindarobackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User Controller", description = "APIs for managing users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Create a new user", description = "Registers a new user in the system")
    @PostMapping
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(userService.registerUser(request));
    }

    @Operation(summary = "Login user", description = "Logs in an existing user")
    @PostMapping("/login")
    public ResponseEntity<UserResponse> loginUser(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(userService.loginUser(request));
    }

    @Operation(summary = "Update user profile", description = "Updates the existing user profile")
    @PutMapping
    public ResponseEntity<UserResponse> updateUserProfile(@Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateUserProfile(request));
    }

    @Operation(summary = "Get user by email", description = "Retrieves a user profile by their email address")
    @GetMapping("/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(
            @Parameter(description = "Email address of the user", required = true)
            @PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }
}

