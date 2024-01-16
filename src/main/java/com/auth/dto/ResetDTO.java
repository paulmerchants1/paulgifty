package com.auth.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ResetDTO {
    private String mobileNo;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
