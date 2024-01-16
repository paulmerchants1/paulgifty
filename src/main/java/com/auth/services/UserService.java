package com.auth.services;

import com.auth.controller.ApiResponse;
import com.auth.entity.UserInfo;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

   // public void saveUser(User user);




    ApiResponse createUser(UserInfo userInfo);
    ApiResponse validateToken(String token);




    //boolean isUserPresentInDatabase(String mobile, String sdkAuthToken);
}
