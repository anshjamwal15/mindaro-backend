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
        UserDto userDto = userService.registerUser(request);
        UserResponse response = ObjectMapperUtils.map(userDto, UserResponse.class);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/exists/{email}")
    public ResponseEntity<Boolean> checkUserExists(@PathVariable String email) {
        boolean exists = userService.isEmailRegistered(email);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
        UserResponse response = ObjectMapperUtils.map(userService.getUserByEmail(email), UserResponse.class);
        return ResponseEntity.ok(response);
    }
}
