package com.dekhokaun.mindarobackend.controller;

import com.dekhokaun.mindarobackend.payload.request.CreateAdminRequest;
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

    @Operation(
        summary = "Create a new admin user",
        description = "Creates a new admin user. Only existing admins can create other admins."
    )
    @PostMapping("/create")
    public ResponseEntity<UserResponse> createAdmin(
            @Valid @RequestBody CreateAdminRequest request,
            @Parameter(description = "Email of the admin creating the new admin")
            @RequestHeader("Admin-Email") String adminEmail) {
        return ResponseEntity.ok(adminService.createAdmin(request, adminEmail));
    }

    @Operation(
        summary = "Get all users",
        description = "Retrieves a list of all users in the system. Only accessible by admins."
    )
    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers(
            @Parameter(description = "Email of the admin requesting the user list")
            @RequestHeader("Admin-Email") String adminEmail) {
        return ResponseEntity.ok(adminService.getAllUsers(adminEmail));
    }

    @Operation(
        summary = "Delete a user",
        description = "Deletes a user from the system. Admins cannot delete other admins."
    )
    @DeleteMapping("/users/{email}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "Email of the user to delete")
            @PathVariable String email,
            @Parameter(description = "Email of the admin requesting the deletion")
            @RequestHeader("Admin-Email") String adminEmail) {
        adminService.deleteUser(email, adminEmail);
        return ResponseEntity.ok().build();
    }
} 