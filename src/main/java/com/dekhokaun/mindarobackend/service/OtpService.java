package com.dekhokaun.mindarobackend.service;

//import com.dekhokaun.mindarobackend.dto.OtpDto;
import com.dekhokaun.mindarobackend.model.Otp;
import com.dekhokaun.mindarobackend.payload.request.OtpRequest;
import com.dekhokaun.mindarobackend.repository.OtpRepository;
import com.dekhokaun.mindarobackend.utils.ObjectMapperUtils;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class OtpService {

    private final OtpRepository otpRepository;

    public OtpService(OtpRepository otpRepository) {
        this.otpRepository = otpRepository;
    }

    // TODO: Set Data according to OTP Type
//    public OtpDto generateOtp(OtpRequest request) {
//        Otp otp = new Otp();
//        otp.setE(request.getEmail());
//        otp.setOtp(generateRandomOtp());
//        otpRepository.save(otp);
//        return ObjectMapperUtils.map(otp, OtpDto.class);
//    }

    // TODO: Fix this function as well
//    public boolean verifyOtp(String email, String otpCode) {
//        Optional<Otp> otp = otpRepository.findByEmailAndOtpCode(email, otpCode);
//        if (otp.isPresent()) {
//            otpRepository.delete(otp.get()); // OTP should be used once
//            return true;
//        }
//        return false;
//    }

    private String generateRandomOtp() {
        return String.valueOf((int) (Math.random() * 9000) + 1000); // 4-digit OTP
    }
}
