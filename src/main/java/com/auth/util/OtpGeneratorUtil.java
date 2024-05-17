package com.auth.util;

// OtpGeneratorUtil.java
import java.util.Random;

public class OtpGeneratorUtil {

    public static String generateOtp() {
        // Generate a 6-digit OTP
        Random random = new Random();
        int otp = 100_000 + random.nextInt(900_000);
        return String.valueOf(otp);
    }
}
