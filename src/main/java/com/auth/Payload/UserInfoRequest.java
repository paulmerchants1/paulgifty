package com.auth.Payload;

import lombok.Data;

@Data
public class UserInfoRequest {
    private String name;
    private String mobileNo;
    private String kitNo;
    private String cardNo;
    private String password;

    // Getters and setters
}
