package com.auth.dto.registoruserdto;


import com.auth.constant.RoleConstants;
import lombok.*;



@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class RegisterRequestDTO {

    private int userId;

    private String name;

    private String userEmail;

    private String userPassword;

    private String userAbout;

    private String userRole = RoleConstants.NORMAL_USER_NAME;

}
