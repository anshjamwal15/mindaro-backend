package com.dekhokaun.mindarobackend.service;

import com.dekhokaun.mindarobackend.exception.InvalidRequestException;
import com.dekhokaun.mindarobackend.exception.ResourceNotFoundException;
import com.dekhokaun.mindarobackend.model.Otp;
import com.dekhokaun.mindarobackend.model.User;
import com.dekhokaun.mindarobackend.payload.request.OtpRequest;
import com.dekhokaun.mindarobackend.payload.response.OtpResponse;
import com.dekhokaun.mindarobackend.repository.OtpRepository;
import com.dekhokaun.mindarobackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class OtpService {

    private static final int OTP_LENGTH = 6;
    private static final int OTP_EXPIRATION_MINUTES = 5;
    private static final SecureRandom random = new SecureRandom();

    private final OtpRepository otpRepository;

    private final UserRepository userRepository;

    public OtpResponse generateOtp(OtpRequest request) {
        int otp = 100000 + random.nextInt(999999);
        String otpMessage = String.format("Your OTP is %s. It is valid for a 10 minutes only.", otp);

        User user = userRepository.findById(UUID.fromString(request.getUserid()))
                .orElseThrow(() -> new InvalidRequestException("User not found with ID: " + request.getUserid()));

        otpRepository.deleteByUseridAndMobileAndOtpType(user.getId(), request.getMobileOrEmail(), request.getOtpType());

        Otp otpEntity = new Otp();
        otpEntity.setUserid(user.getId());
        otpEntity.setOtpType(request.getOtpType());

        if (request.getOtpType().equalsIgnoreCase("mobile")) {
            // sms service function
        } else if (request.getOtpType().equalsIgnoreCase("email")) {
            // email service function
        }

        otpEntity.setCountryCodeMobile(request.getCountryCode());
        otpEntity.setMobile(request.getMobileOrEmail());
        otpEntity.setOtp(String.valueOf(otp));

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


    private int sendSmsRequest(String mobileNumber, String username, String otp) throws IOException {
        String apiUrl = "https://we8.in/smsapi/smsapi.php";

        String jsonPayload = String.format("""
        {
            "api1": "otp",
            "data1": {
                "usercode": "advijr",
                "smsuser": "%s",
                "smskey": "Ymjsd5h7@3",
                "mobiles": "%s",
                "message": "OTP for login is %s, valid for 10 minutes. -Serviced by Mindaro",
                "senderid": "MINDRO",
                "entityid": "1501544040000010555",
                "tempid": "1107172551760125165",
                "v1": "stage_of_sending_sms",
                "v2": "user_id_reference",
                "v3": "any_value",
                "v4": "any_value",
                "v5": "datetimesent"
            },
            "chksum1": "chksum"
        }
        """, username, mobileNumber, otp);

        URL url = new URL(apiUrl);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        System.out.println("Response Code: " + responseCode);
        return responseCode;
    }
}
