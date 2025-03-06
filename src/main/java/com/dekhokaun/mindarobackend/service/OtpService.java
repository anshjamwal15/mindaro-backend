package com.dekhokaun.mindarobackend.service;

import com.dekhokaun.mindarobackend.model.Otp;
import com.dekhokaun.mindarobackend.payload.request.OtpRequest;
import com.dekhokaun.mindarobackend.payload.response.OtpResponse;
import com.dekhokaun.mindarobackend.repository.OtpRepository;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.util.Optional;

@Service
public class OtpService {

    private static final int OTP_LENGTH = 6;
    private static final int OTP_EXPIRATION_MINUTES = 5;
    private static final SecureRandom random = new SecureRandom();

    private OtpRepository otpRepository;

    public OtpResponse generateOtp(OtpRequest request) {
        String otp = String.format("%06d", random.nextInt(999999));
        String otpMessage = String.format("Your OTP code is %s. Please do not share this code with anyone. It is valid for a limited time.", otp);

        otpRepository.deleteByUseridAndMobileAndOtpType(request.getUserid(), request.getMobileOrEmail(), request.getOtpType());

        Otp otpEntity = new Otp();
        otpEntity.setUserid(request.getUserid());
        otpEntity.setOtpType(request.getOtpType());

        if (request.getOtpType().equalsIgnoreCase("mobile")) {
            // sms service function
        } else if (request.getOtpType().equalsIgnoreCase("email")) {
            // email service function
        }

        otpEntity.setCountryCodeMobile(request.getCountryCode());
        otpEntity.setMobile(request.getMobileOrEmail());
        otpEntity.setOtp(otp);

        if (!request.getIp().isBlank()) {
            otpEntity.setIp(request.getIp());
        }

        // TODO: counter use case discuss
        otpEntity.setSmsCounter("0");

        // TODO: add sms response from sms service
        otpEntity.setSmsResponse(otpMessage);
        otpEntity.setSmsapi("example_api");
        otpEntity.setStatus("PENDING");

        otpRepository.save(otpEntity);
        return new OtpResponse(otpMessage);
    }

    public OtpResponse verifyOtp(OtpRequest request) {
        Optional<Otp> storedOtp = otpRepository.findByUseridAndMobileAndOtpType(request.getUserid(), request.getMobileOrEmail(), request.getOtpType());

        if (storedOtp.isPresent() && storedOtp.get().getOtp().equals(request.getOtp())) {
//            otpRepository.deleteByUseridAndMobileAndOtpType(userid, mobile, otpType);
            return new OtpResponse("OTP verified successfully!");
        }
        return new OtpResponse("OTP verification failed!");
    }
}
