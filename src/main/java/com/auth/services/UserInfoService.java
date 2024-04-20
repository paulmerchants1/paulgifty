package com.auth.services;




import com.auth.entity.UserInfo;

import java.util.List;

public interface UserInfoService {
    List<UserInfo> findByMobileNo(String mobileNo);
    void save(UserInfo userInfo);

    boolean verifyUserCredentials(String mobileNo, String password);

//    UserInfo findByMobileNo(String mobileNo);
}