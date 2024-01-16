package com.auth.services.impl;

import com.auth.Exception.InvalidMobileNumberException;
import com.auth.controller.ApiResponse;
import com.auth.entity.UserInfo;
import com.auth.repo.UserInfoRepository;
import com.auth.services.UserService;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.Date;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserInfoRepository userInfoRepository;

    @Autowired
    public UserServiceImpl(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    private boolean isAnyFieldBlank(String... fields) {
        for (String field : fields) {
            if (StringUtils.isBlank(field)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ApiResponse createUser(UserInfo userInfo) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            log.info("Creating user: {}", userInfo);

            // Check if any field is blank (mobile number, card number, etc.)
            if (isAnyFieldBlank(userInfo.getMobileNo(), userInfo.getCardNo(), userInfo.getName(), userInfo.getKitNo(), userInfo.getPassword())) {
                log.warn("Bad credentials: {}", userInfo);
                apiResponse.setMessage("Bad credentials");
                return apiResponse;
            }

            // Check if the mobile number has exactly 10 digits
            if (!isValidMobileNumber(userInfo.getMobileNo())) {
                throw new InvalidMobileNumberException("Invalid mobile number format. Please enter a 10-digit mobile number.");
            }

            // Check if a user with the same mobile number or card number already exists
            UserInfo userExists = userInfoRepository.findByMobileNoAndCardNo(userInfo.getMobileNo(), userInfo.getCardNo());

            if (userExists == null) {
                log.info("User does not exist. Creating a new user: {}", userInfo);

                UserInfo byCard = userInfoRepository.findByCardNo(userInfo.getCardNo());

                if (!(byCard == null)) {
                    log.warn("Duplicate card number found: {}", userInfo.getCardNo());
                    apiResponse.setMessage("Please Check Your Mobile No");
                    return apiResponse;
                }

                // Continue with user creation logic if no duplicate is found
                String token = generateJwtToken(userInfo.getMobileNo());
                userInfo.setToken(token);
                userInfoRepository.save(userInfo);
                apiResponse.setMessage("SDKAuth Token!");
                apiResponse.setToken(token);
                log.info("User created successfully. SDKAuth Token generated: {}", token);
            } else {
                if (userInfo.getMobileNo().equals(userExists.getMobileNo()) && userInfo.getCardNo().equals(userInfo.getCardNo())) {
                    log.info("User already exists. Updating SDKAuth Token: {}", userInfo);
                    String token = generateJwtToken(userInfo.getMobileNo());
                    userExists.setToken(token);
                    userInfoRepository.save(userExists);
                    apiResponse.setMessage("SDKAuth Token!");
                    apiResponse.setToken(token);
                    log.info("SDKAuth Token updated successfully: {}", token);
                }
            }
        } catch (InvalidMobileNumberException e) {
            log.error("Invalid mobile number format: {}", e.getMessage());
            apiResponse.setMessage(e.getMessage());
        }
        return apiResponse;
    }

    @Override
    public ApiResponse validateToken(String token) {
        log.info("Validating token: {}", token);
        UserInfo user = userInfoRepository.findByToken(token);

        if (user != null) {
            log.info("Token is valid for user: {}", user);
            return new ApiResponse("Token is valid!", null);
        } else {
            log.warn("Token is invalid: {}", token);
            return new ApiResponse("Token is invalid!", null);
        }
    }

    private String generateJwtToken(String mobileNo) {
        // Implement JWT token generation logic here
        String key = mobileNo + new Date().getTime();
        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final byte[] hash = digest.digest(key.getBytes("UTF-8"));
            final StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                final String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.substring(0, 15);
        } catch (Exception ex) {
            log.error("Error generating JWT token: {}", ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    private boolean isValidMobileNumber(String mobileNo) {
        // Check if the mobile number has exactly 10 digits
        return mobileNo != null && mobileNo.matches("\\d{12}");
    }
}
