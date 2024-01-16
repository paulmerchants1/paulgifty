package com.auth.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class LoginDTO {

    private String mobileNo;
    private String password;

}
