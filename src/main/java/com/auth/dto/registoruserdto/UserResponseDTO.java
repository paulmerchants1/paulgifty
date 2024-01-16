package com.auth.dto.registoruserdto;



import com.auth.dto.roledto.RoleDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Component
public class UserResponseDTO {
    private int userId;
    private String name;
    private String userEmail;

    private String userPassword;
    private String userAbout;

    private Set<RoleDTO> roles = new HashSet<>();



    @JsonIgnore
    public String getUserPassword() {
        return this.userPassword;
    }

    @JsonProperty
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

}
