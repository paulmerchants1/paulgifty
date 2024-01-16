package com.auth.controller;


import com.auth.Payload.CustomerDetailsResponse;
import com.auth.Payload.VerificationResponse;
import com.auth.dto.LoginDTO;
import com.auth.dto.ResetDTO;
import com.auth.dto.Response;
import com.auth.dto.SetPasswordDTO;
import com.auth.dto.sdkdto.MobileNoDTO;
import com.auth.entity.UserInfo;
import com.auth.entity.VerificationRequest;
import com.auth.services.GiftyService;
import com.auth.services.UserInfoService;
import com.auth.services.UserService;
import com.auth.services.impl.VerificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/user")
public class UserController {


    private final GiftyService giftyService;

    private final UserService userService;

    private final VerificationService verificationService;

    private final UserInfoService userInfoService;

    @Autowired
    public UserController( GiftyService giftyService, UserService userService, VerificationService verificationService, UserInfoService userInfoService) {

        this.giftyService = giftyService;
        this.userInfoService=userInfoService;
        this.userService = userService;
        this.verificationService = verificationService;
    }
    @GetMapping("/login_test")
    public String home()
    {
        return "welcome";
    }

    @GetMapping("test")
    public String test(){
        return" Hello , this is for testing";
    }

    @PostMapping("/sdk-token")
    public ResponseEntity<Response> createSDKToken(@RequestBody MobileNoDTO mobileNoDTO) {
        log.info("Received request to create SDK token");
        Response response = giftyService.createSDKToken(mobileNoDTO);
        log.info("SDK token created successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PostMapping("/verify")
    public ResponseEntity<Object> verifyUser(@RequestBody VerificationRequest request) {
        log.info("Received request to verify user");
        VerificationResponse response = verificationService.verifyUser(request.getEventId(), request.getMobileNo(), request.getSdkAuthToken());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8");
        log.info("Verification response: {}", response);

        Map<String, Object> responseBody = new HashMap<>();
        if (response.isSuccess()) {
            log.info("User verification successful");
            responseBody.put("Success", true);
            responseBody.put("allowSubBusinessCards", true);
            responseBody.put("issuer", "PAULM");
            return new ResponseEntity<>(responseBody, headers, HttpStatus.OK);
        } else {
            log.info("User verification failed");
            responseBody.put("Success", true);
            responseBody.put("allowSubBusinessCards", true);
            responseBody.put("issuer", "PAULM");
            return new ResponseEntity<>(responseBody, headers, HttpStatus.OK);
        }
    }
    @PostMapping("/signIn")
    public ResponseEntity<Response> loginUser(@RequestBody LoginDTO loginDTO) {

        log.info("=>> UserController:: Inside loginUser Method <<=");
        Response response = giftyService.loginUser(loginDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
    @PostMapping("/set-password")
    public ResponseEntity<Response> setPassword(@RequestBody SetPasswordDTO setPasswordDTO) {

        log.info("=>> UserController:: Inside setPassword <<=");
        Response response = giftyService.setPassword(setPasswordDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/reset-password")
    public ResponseEntity<Response> resetPassword(@RequestBody ResetDTO resetDTO) {

        log.info("==>> UserController:: Inside resetPassword Method <<==");
        Response response = giftyService.resetPassword(resetDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }




    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createUser(@RequestBody UserInfo userInfo) {
        log.info("Received request to create user");
        ApiResponse response = userService.createUser(userInfo);
        log.info("User created successfully");
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<List<CustomerDetailsResponse>> getCustomerDetails(@RequestParam String mobileNo) {
        // Check if the mobile number exists in the UserInfo table
        List<UserInfo> userInfo = userInfoService.findByMobileNo(mobileNo);
        if (userInfo.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<CustomerDetailsResponse> lCustomerDetailsResponse = new ArrayList<>();

        for (UserInfo uInfo : userInfo) {
            // Map the user details to the response DTO
            CustomerDetailsResponse response = new CustomerDetailsResponse();
            response.setId(uInfo.getId());
            response.setName(uInfo.getName());
            response.setMobileNo(uInfo.getMobileNo());
            response.setKitNo(uInfo.getKitNo());
            response.setCardNo(uInfo.getCardNo());
            response.setPassword(uInfo.getPassword());
            response.setToken(uInfo.getToken());

            lCustomerDetailsResponse.add(response);
        }


        return ResponseEntity.ok(lCustomerDetailsResponse);
    }

}

