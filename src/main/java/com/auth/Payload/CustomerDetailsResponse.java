package com.auth.Payload;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomerDetailsResponse {
    private Long id;
    private String name;
    private String mobileNo;
    private String kitNo;
    private String cardNo;
    private String password;
    private String token;


    // Constructors, getters, and setters
}
