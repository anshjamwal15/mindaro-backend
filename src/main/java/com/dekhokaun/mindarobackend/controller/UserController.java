package com.dekhokaun.mindarobackend.controller;

import com.dekhokaun.mindarobackend.dto.UserDto;
import com.dekhokaun.mindarobackend.payload.request.UserRequest;
import com.dekhokaun.mindarobackend.payload.response.UserResponse;
import com.dekhokaun.mindarobackend.service.UserService;
import com.dekhokaun.mindarobackend.utils.ObjectMapperUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.authenticateOrRegister(request));
    }

    @PostMapping("/update")
    public ResponseEntity<UserResponse> updateUserProfile(@RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.updateUserProfile(request));
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
        UserResponse response = ObjectMapperUtils.map(userService.getUserByEmail(email), UserResponse.class);
        return ResponseEntity.ok(response);
    }
}
