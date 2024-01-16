package com.blog.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ResetPasswordRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mobileNo;
    private String appPass1;
    private String appPass2;

    // Constructors, getters, and setters

    public ResetPasswordRequest() {
        // Default constructor
    }

    public ResetPasswordRequest(String mobileNo, String appPass1, String appPass2) {
        this.mobileNo = mobileNo;
        this.appPass1 = appPass1;
        this.appPass2 = appPass2;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getAppPass1() {
        return appPass1;
    }

    public void setAppPass1(String appPass1) {
        this.appPass1 = appPass1;
    }

    public String getAppPass2() {
        return appPass2;
    }

    public void setAppPass2(String appPass2) {
        this.appPass2 = appPass2;
    }

    public String getNewPassword() {
        return getNewPassword();
    }
}

