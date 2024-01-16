package com.auth.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class PasswordManager {

    @Id
    @GeneratedValue
    private Long id;


    private String mobileNo;
    private String password;
}
