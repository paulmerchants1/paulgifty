package com.auth.services.impl;

// OtpService.java

import com.auth.Util.OtpGeneratorUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("customOtpService")
public class OtpService {
    private final Map<String, String> otpStorage = new HashMap<>();

    public String generateOtp(String mobileNumber) {
        String generatedOtp = OtpGeneratorUtil.generateOtp();
        otpStorage.put(mobileNumber, generatedOtp);
        return generatedOtp;
    }

    public boolean verifyOtp(String mobileNumber, String userEnteredOtp) {
        String storedOtp = otpStorage.get(mobileNumber);
        return storedOtp != null && storedOtp.equals(userEnteredOtp);
    }
}
