package com.dekhokaun.mindarobackend.controller;

import com.dekhokaun.mindarobackend.payload.request.CreateAdminRequest;
import com.dekhokaun.mindarobackend.payload.request.CreateUserRequest;
import com.dekhokaun.mindarobackend.payload.request.AdminUpdateUserRequest;
import com.dekhokaun.mindarobackend.payload.response.UserResponse;
import com.dekhokaun.mindarobackend.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin Controller", description = "APIs for admin operations")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @Operation(summary = "Create a new admin user", description = "Creates a new admin user. Only existing admins can create other admins.")
    @PostMapping("/create")
    public ResponseEntity<UserResponse> createAdmin(
            @Valid @RequestBody CreateAdminRequest request,
            @Parameter(description = "Email of the admin creating the new admin") @RequestHeader("Admin-Email") String adminEmail) {
        return ResponseEntity.ok(adminService.createAdmin(request, adminEmail));
    }

    @Operation(summary = "Get all users", description = "Retrieves a list of all users in the system. Only accessible by admins.")
    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers(
            @Parameter(description = "Email of the admin requesting the user list") @RequestHeader("Admin-Email") String adminEmail) {
        return ResponseEntity.ok(adminService.getAllUsers(adminEmail));
    }

    @Operation(summary = "Delete a user", description = "Deletes a user from the system. Admins cannot delete other admins.")
    @DeleteMapping("/users/{email}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "Email of the user to delete") @PathVariable String email,
            @Parameter(description = "Email of the admin requesting the deletion") @RequestHeader("Admin-Email") String adminEmail) {
        adminService.deleteUser(email, adminEmail);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Create a new user", description = "Creates a new user in the system. Only admins can create users.")
    @PostMapping("/users")
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody CreateUserRequest request,
            @Parameter(description = "Email of the admin creating the user") @RequestHeader("Admin-Email") String adminEmail) {
        return ResponseEntity.ok(adminService.createUser(request, adminEmail));
    }

    @Operation(summary = "Get user by email", description = "Retrieves a specific user by their email address. Only accessible by admins.")
    @GetMapping("/users/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(
            @Parameter(description = "Email of the user to retrieve") @PathVariable String email,
            @Parameter(description = "Email of the admin requesting the user details") @RequestHeader("Admin-Email") String adminEmail) {
        return ResponseEntity.ok(adminService.getUserByEmail(email, adminEmail));
    }

    @Operation(summary = "Update a user", description = "Updates an existing user's information. Only admins can update users.")
    @PutMapping("/users/{email}")
    public ResponseEntity<UserResponse> updateUser(
            @Parameter(description = "Email of the user to update") @PathVariable String email,
            @Valid @RequestBody AdminUpdateUserRequest request,
            @Parameter(description = "Email of the admin updating the user") @RequestHeader("Admin-Email") String adminEmail) {
        return ResponseEntity.ok(adminService.updateUser(email, request, adminEmail));
    }
}