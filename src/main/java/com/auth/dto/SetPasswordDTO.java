package com.auth.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class SetPasswordDTO {
    private String mobileNo;
    private String appPass1;
    private String appPass2;
}
