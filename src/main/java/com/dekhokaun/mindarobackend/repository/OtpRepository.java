package com.dekhokaun.mindarobackend.repository;

import com.dekhokaun.mindarobackend.model.Otp;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OtpRepository extends BaseRepository<Otp, Integer> {

    List<Otp> findByMobile(String mobile);

    List<Otp> findByStatus(String status);
}
