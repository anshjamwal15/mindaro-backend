package com.dekhokaun.mindarobackend.controller;

import com.dekhokaun.mindarobackend.payload.request.OtpRequest;
import com.dekhokaun.mindarobackend.payload.response.OtpResponse;
import com.dekhokaun.mindarobackend.service.OtpService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/otp")
public class OtpController {

    private final OtpService otpService;

    public OtpController(OtpService otpService) {
        this.otpService = otpService;
    }

//    @PostMapping("/send")
//    public ResponseEntity<OtpResponse> sendOtp(@RequestBody OtpRequest request) {
//        return ResponseEntity.ok(otpService.(request));
//    }

//    @PostMapping("/verify")
//    public ResponseEntity<OtpResponse> verifyOtp(@RequestBody OtpRequest request) {
//        return ResponseEntity.ok(otpService.verifyOtp(request));
//    }
}
