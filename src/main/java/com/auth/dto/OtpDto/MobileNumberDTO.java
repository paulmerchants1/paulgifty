package com.auth.dto.OtpDto;

import lombok.Data;

@Data
// MobileNumberDTO.java
public class MobileNumberDTO {
    private String mobileNumber;

    // Default constructor (needed for JSON deserialization)
    public MobileNumberDTO() {
    }

    public MobileNumberDTO(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}

