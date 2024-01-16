package com.auth.services.impl;

import com.auth.Payload.MobileValidationResult;
import com.auth.Payload.VerificationResponse;
import com.auth.entity.UserVerification;
import com.auth.entity.skdentity.SdkResponse;
import com.auth.repo.SdkResponseRepo;
import com.auth.repo.UserVerificationRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class VerificationService {

    private final SdkResponseRepo sdkResponseRepo;
    private final UserVerificationRepository userVerificationRepository;

    @Autowired
    public VerificationService( SdkResponseRepo sdkResponseRepo, UserVerificationRepository userVerificationRepository) {

        this.sdkResponseRepo = sdkResponseRepo;
        this.userVerificationRepository = userVerificationRepository;
    }

    // Implement your mobile number validation logic here
    private MobileValidationResult validateMobileNumber(String mobileNo) {
        log.info("Validating mobile number: {}", mobileNo);

        if (mobileNo != null && mobileNo.matches("\\d{12}")) {
            log.info("Mobile number validation successful");
            return new MobileValidationResult(true, "Valid mobile number");
        } else {
            log.info("Mobile number validation failed. Must be 12 digits");
            return new MobileValidationResult(false, "Mobile number must be 12 digits");
        }
    }
    @Transactional
    public VerificationResponse verifyUser(String eventId, String mobileNo, String token) {
        log.info("Verifying user with eventId: {}, mobileNo: {}, token: {}", eventId, mobileNo, token);

        // Validate mobile number
        MobileValidationResult mobileValidationResult = validateMobileNumber(mobileNo);
        if (!mobileValidationResult.isValid()) {
            log.info("Mobile number validation failed: {}", mobileValidationResult.getMessage());
            return new VerificationResponse(false);
        }

        // Check if the user exists in UserInfo
        SdkResponse sdkResponse = sdkResponseRepo.findByMobileNoAndSdkToken(mobileNo, token);

        if (sdkResponse != null) {
            log.info("User found in UserInfo with matching mobileNo: {} and token: {}", mobileNo, token);

            // Check for existing verification record
            UserVerification existingVerification = userVerificationRepository
                    .MobileNoAndToken(mobileNo, token);

            if (existingVerification != null) {
                // If record exists, update it
                existingVerification.setEventId(eventId);
                existingVerification.setMobileNo(mobileNo);
                existingVerification.setToken(token);
                userVerificationRepository.save(existingVerification);
                log.info("Existing verification record updated");
            } else {
                // If no existing record, create a new one
                UserVerification userVerification = new UserVerification();
                userVerification.setEventId(eventId);
                userVerification.setMobileNo(mobileNo);
                userVerification.setToken(token);
                userVerificationRepository.save(userVerification);
                log.info("New verification record created");
            }

            return new VerificationResponse(true);
        } else {
            // User not found in UserInfo
            log.info("User not found in UserInfo with mobileNo: {} and token: {}", mobileNo, token);
            return new VerificationResponse(false);
        }
    }


}
