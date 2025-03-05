package com.dekhokaun.mindarobackend.controller;

import com.dekhokaun.mindarobackend.dto.SessionDto;
import com.dekhokaun.mindarobackend.service.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/session")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SessionDto>> getSessionsByUser(@PathVariable String userId) {
        return ResponseEntity.ok(sessionService.getUserSessions(userId));
    }
}
