package com.auth.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "SignIn_data") // Specify the table name here
public class UserData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mobileNo;
    private String password;

    public UserData() {
    }

    public UserData(String mobileNo, String password) {
        this.mobileNo = mobileNo;
        this.password = password;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
