package com.dekhokaun.mindarobackend.controller;

import com.dekhokaun.mindarobackend.payload.response.SessionResponse;
import com.dekhokaun.mindarobackend.service.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/session")
@Tag(name = "Session Controller", description = "APIs for managing user sessions")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Operation(summary = "Get sessions by user ID", description = "Fetch all sessions associated with a given user ID")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SessionResponse>> getSessionsByUser(@PathVariable String userId) {
        return ResponseEntity.ok(sessionService.getUserSessions(userId));
    }
}
