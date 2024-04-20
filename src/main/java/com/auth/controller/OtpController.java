package com.auth.controller;

// OtpController.java



import com.auth.dto.OtpDto.MobileNumberDTO;
import com.auth.dto.OtpDto.VerifyOtpDTO;
import com.auth.services.impl.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class OtpController {

    private final OtpService customOtpService;

    @Autowired
    public OtpController(@Qualifier("customOtpService") OtpService customOtpService) {
        this.customOtpService = customOtpService;
    }

    @PostMapping("/generateOtp")
    public String generateOtp(@RequestBody MobileNumberDTO mobileNumberDTO) {
        String mobileNumber = mobileNumberDTO.getMobileNumber();
        return customOtpService.generateOtp(mobileNumber);
    }

    @PostMapping("/verifyOtp")
    public String verifyOtp(@RequestBody VerifyOtpDTO verifyOtpDTO) {
        String mobileNumber = verifyOtpDTO.getMobileNumber();
        String userEnteredOtp = verifyOtpDTO.getOtp();

        if (customOtpService.verifyOtp(mobileNumber, userEnteredOtp)) {
            return "OTP verification successful!";
        } else {
            return "OTP verification failed. Please check the entered OTP.";
        }
    }
}
