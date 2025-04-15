package com.dekhokaun.mindarobackend.service;

import com.dekhokaun.mindarobackend.model.Otp;
import com.dekhokaun.mindarobackend.model.User;
import com.dekhokaun.mindarobackend.payload.request.OtpRequest;
import com.dekhokaun.mindarobackend.payload.response.OtpResponse;
import com.dekhokaun.mindarobackend.repository.OtpRepository;
import com.dekhokaun.mindarobackend.repository.UserRepository;
import com.dekhokaun.mindarobackend.utils.RegexUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

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

        long mobile;
        if (request.getMobileOrEmail() != null && RegexUtils.isNumeric(request.getMobileOrEmail())) {
            mobile = Long.parseLong(request.getMobileOrEmail());
        } else {
            mobile = 0;
        }

        User user = userRepository.findByMobile(mobile)
                .orElseGet(() -> {
                    // Create and save a new user if not exists
                    User newUser = new User();
                    newUser.setName(request.getName());
                    newUser.setMobile(mobile);
                    return userRepository.save(newUser);
                });

        otpRepository.deleteByUseridAndMobileAndOtpType(user.getId(), request.getMobileOrEmail(), request.getOtpType());

        // Create new OTP entry
        Otp otpEntity = new Otp();
        otpEntity.setUserid(user.getId());
        otpEntity.setOtpType(request.getOtpType());
        otpEntity.setCountryCodeMobile("India");
        otpEntity.setMobile(request.getMobileOrEmail());
        otpEntity.setOtp(String.valueOf(otp));
        otpEntity.setSmsCounter("0");
        otpEntity.setSmsapi("example_api");
        otpEntity.setStatus("PENDING");

        if (!request.getIp().isBlank()) {
            otpEntity.setIp(request.getIp());
        }

        try {
            if ("mobile".equalsIgnoreCase(request.getOtpType())) {
                String response = sendMessage("mobile", String.valueOf(otp));
                System.out.println("This is the otp response : " + response);
                otpEntity.setSmsResponse("response");
            } else if ("email".equalsIgnoreCase(request.getOtpType())) {
                // Call email service function here
            }
        } catch (Exception e) {
            otpEntity.setSmsResponse("Otp sending failed: " + e.getMessage());
            log.error("Error sending OTP: {}", e.getMessage());
        }

        otpRepository.save(otpEntity);
        return new OtpResponse("Otp sent successfully");
    }

//
//    public OtpResponse verifyOtp(OtpRequest request) {
//        if (request.getOtp() == null || request.getOtp().isEmpty()) {
//            return new OtpResponse("OTP is required!");
//        }
//
//        Optional<Otp> storedOtp = otpRepository.findByUseridAndMobileAndOtpTypeAndStatus(
//                UUID.fromString(request.getUserid()), request.getMobileOrEmail(), request.getOtpType(), "PENDING"
//        );
//
//        if (storedOtp.isPresent() && storedOtp.get().getOtp().equals(request.getOtp())) {
//            storedOtp.get().setStatus("VERIFIED");
//            otpRepository.save(storedOtp.get());
//            return new OtpResponse("OTP verified successfully!");
//        }
//
//        return new OtpResponse("OTP verification failed!");
//    }

    public static String sendMessage(String phoneNumber, String otpCode) {
        HttpURLConnection connection = null;

        String data1 = URLEncoder.encode("{\"usercode\":\"advijr\",\"smsuser\":\"username\",\"smskey\":\"Ymjsd5h7@3\","
                + "\"mobiles\":\"" + phoneNumber + "\","
                + "\"message\":\"OTP for login is " + otpCode + ", valid for 10 minutes. -Serviced by Mindaro\","
                + "\"senderid\":\"MINDRO\",\"entityid\":\"1501544040000010555\",\"tempid\":\"1107172551760125165\","
                + "\"v1\":\"stage_of_sending_sms\",\"v2\":\"user_id_reference\",\"v3\":\"any_value\","
                + "\"v4\":\"any_value\",\"v5\":\"datetimesent\"}", StandardCharsets.UTF_8);

        String urlEncodedParams = "api1=otp&data1=" + data1 + "&chksum1=" + URLEncoder.encode("chksum", StandardCharsets.UTF_8);

        try {
            URL url = new URL("https://we8.in/smsapi/smsapi.php");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = urlEncodedParams.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            StringBuilder response = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }

            if (responseCode == HttpURLConnection.HTTP_OK) {
                return "Request Successful: " + response;
            } else {
                return "Request Failed: " + responseCode;
            }
        } catch (IOException e) {
            log.error("Error sending SMS request: {}", e.getMessage());
            return "Exception: " + e.getMessage();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
