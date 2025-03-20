package com.dekhokaun.mindarobackend.service;

import com.dekhokaun.mindarobackend.exception.InvalidRequestException;
import com.dekhokaun.mindarobackend.model.Otp;
import com.dekhokaun.mindarobackend.model.User;
import com.dekhokaun.mindarobackend.payload.request.OtpRequest;
import com.dekhokaun.mindarobackend.payload.response.OtpResponse;
import com.dekhokaun.mindarobackend.repository.OtpRepository;
import com.dekhokaun.mindarobackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OtpService {

    private static final SecureRandom random = new SecureRandom();
    private static final Logger log = LoggerFactory.getLogger(OtpService.class);

    private final OtpRepository otpRepository;

    private final UserRepository userRepository;

    @Transactional
    public OtpResponse generateOtp(OtpRequest request) {
        int otp = 100000 + random.nextInt(900000);
        String otpMessage = String.format("OTP for login is %d, valid for 10 minutes. -Serviced by Mindaro", otp);

        User user = userRepository.findById(UUID.fromString(request.getUserid()))
                .orElseThrow(() -> new InvalidRequestException("User not found with ID: " + request.getUserid()));

        // Delete previous OTPs for this user
        otpRepository.deleteByUseridAndMobileAndOtpType(user.getId(), request.getMobileOrEmail(), request.getOtpType());

        // Create new OTP entity
        Otp otpEntity = new Otp();
        otpEntity.setUserid(user.getId());
        otpEntity.setOtpType(request.getOtpType());
        otpEntity.setCountryCodeMobile(request.getCountryCode());
        otpEntity.setMobile(request.getMobileOrEmail());
        otpEntity.setOtp(String.valueOf(otp));

        if (!request.getIp().isBlank()) {
            otpEntity.setIp(request.getIp());
        }

        otpEntity.setSmsCounter("0");
        otpEntity.setSmsapi("example_api");
        otpEntity.setStatus("PENDING");

        // Send OTP via SMS or Email
        if ("mobile".equalsIgnoreCase(request.getOtpType())) {
            try {
                // sms service
//                int responseCode =
//                otpEntity.setSmsResponse("SMS sent with response code: " + responseCode);
            } catch (Exception e) {
                otpEntity.setSmsResponse("SMS sending failed: " + e.getMessage());
                log.error(e.getMessage());
            }
        } else if ("email".equalsIgnoreCase(request.getOtpType())) {
            // Call email service function here
        }

        otpRepository.save(otpEntity);
        return new OtpResponse(otpMessage);
    }

    public OtpResponse verifyOtp(OtpRequest request) {
        if (request.getOtp() == null || request.getOtp().isEmpty()) {
            return new OtpResponse("OTP is required!");
        }

        Optional<Otp> storedOtp = otpRepository.findByUseridAndMobileAndOtpTypeAndStatus(
                UUID.fromString(request.getUserid()), request.getMobileOrEmail(), request.getOtpType(), "PENDING"
        );

        if (storedOtp.isPresent() && storedOtp.get().getOtp().equals(request.getOtp())) {
            // otpRepository.deleteByUseridAndMobileAndOtpType(request.getUserid(), request.getMobileOrEmail(), request.getOtpType());
            storedOtp.get().setStatus("VERIFIED");
            otpRepository.save(storedOtp.get());
            return new OtpResponse("OTP verified successfully!");
        }

        return new OtpResponse("OTP verification failed!");
    }
}
