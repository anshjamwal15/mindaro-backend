package com.dekhokaun.mindarobackend.repository;

import com.dekhokaun.mindarobackend.model.Otp;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OtpRepository extends BaseRepository<Otp, Integer> {

    // mobile == email
    Optional<Otp> findByUseridAndMobileAndOtpType(String userid, String mobileOrEmail, String otpType);

    void deleteByUseridAndMobileAndOtpType(String userid, String mobileOrEmail, String otpType);
}
    