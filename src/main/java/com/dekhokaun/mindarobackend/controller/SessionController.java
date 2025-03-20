package com.dekhokaun.mindarobackend.controller;

import com.dekhokaun.mindarobackend.payload.request.SessionRequest;
import com.dekhokaun.mindarobackend.payload.response.SessionResponse;
import com.dekhokaun.mindarobackend.service.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/session")
@Tag(name = "Session Controller", description = "APIs for managing user sessions")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Operation(summary = "Create a new session", description = "Creates a new user session and returns session details")
    @PostMapping("/create")
    public ResponseEntity<SessionResponse> createSession(@Valid @RequestBody SessionRequest request) {
        SessionResponse sessionResponse = sessionService.createSession(request);
        return ResponseEntity.ok(sessionResponse);
    }

    @Operation(summary = "Get sessions by user ID", description = "Fetch all sessions associated with a given user ID")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SessionResponse>> getSessionsByUser(@PathVariable String userId) {
        return ResponseEntity.ok(sessionService.getUserSessions(userId));
    }

    @Operation(summary = "Update logout time", description = "Updates the logout time of a user session")
    @PutMapping("/logout/{sessionId}")
    public ResponseEntity<Void> updateLogoutTime(@PathVariable String sessionId) {
        sessionService.updateLogoutTime(sessionId);
        return ResponseEntity.ok().build();
    }
}
