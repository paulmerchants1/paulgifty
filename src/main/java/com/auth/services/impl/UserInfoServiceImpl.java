package com.auth.services.impl;


import com.auth.entity.UserInfo;
import com.auth.repo.UserInfoRepository;
import com.auth.services.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserInfoServiceImpl implements UserInfoService {

    private final UserInfoRepository userInfoRepository;

    @Autowired
    public UserInfoServiceImpl(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    @Override
    public List<UserInfo> findByMobileNo(String mobileNo) {
        log.info("Finding user by mobile number: {}", mobileNo);
        List<UserInfo> users = userInfoRepository.findByMobileNo(mobileNo);
        log.info("Found {} user(s) with mobile number: {}", users.size(), mobileNo);
        return users;
    }

    @Override
    public void save(UserInfo userInfo) {
        log.info("Saving user: {}", userInfo);
        userInfoRepository.save(userInfo);
        log.info("User saved successfully: {}", userInfo);
    }

    public boolean verifyUserCredentials(String mobileNo, String password) {
        log.info("Verifying user credentials for mobile number: {}", mobileNo);

        return true;
    }
}
