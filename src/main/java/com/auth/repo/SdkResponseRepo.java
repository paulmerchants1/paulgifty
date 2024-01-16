package com.auth.repo;


import com.auth.entity.skdentity.SdkResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

@EnableJpaRepositories
public interface SdkResponseRepo extends JpaRepository<SdkResponse, Long> {
    Optional<SdkResponse> findByMobileNo(String mobileNo);



   SdkResponse findByMobileNoAndSdkToken(String mobileNo, String token);

//    Optional<SdkResponse> findByMobileNoAndSdkToken(String mobileNo, String sdkToken);
}
