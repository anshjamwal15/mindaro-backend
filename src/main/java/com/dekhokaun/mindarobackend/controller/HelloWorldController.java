package com.dekhokaun.mindarobackend.controller;

import com.dekhokaun.mindarobackend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloWorldController {

    private final UserService userService;

    public HelloWorldController(UserService userService) {this.userService = userService;}

    @GetMapping("/hello")
    public String helloWorld() {
        userService.createTestUser();
        return "Hello World!";
    }
}
