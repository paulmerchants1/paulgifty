package com.blog.entity;

import lombok.Data;

@Data
public class VerificationRequest {
    private String eventId;
    private String mobileNo;
    private String sdkAuthToken;


}
