package com.auth.repo;


import com.auth.entity.UserVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserVerificationRepository extends JpaRepository<UserVerification, Long> {
    boolean existsByEventIdAndMobileNoAndToken(String eventId, String mobileNo, String token);

    UserVerification MobileNoAndToken( String mobileNo, String token);
    // You can add custom query methods if needed

}