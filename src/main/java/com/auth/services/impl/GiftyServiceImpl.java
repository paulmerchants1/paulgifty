package com.auth.services.impl;


import com.auth.dto.LoginDTO;
import com.auth.dto.ResetDTO;
import com.auth.dto.Response;
import com.auth.dto.SetPasswordDTO;
import com.auth.dto.sdkdto.MobileNoDTO;

import com.auth.entity.PasswordManager;
import com.auth.entity.UserData;
import com.auth.entity.skdentity.SdkResponse;
import com.auth.repo.PasswordManagerRepo;
import com.auth.repo.SdkResponseRepo;
import com.auth.repo.UserDataRepository;
import com.auth.repo.UserInfoRepository;
import com.auth.services.GiftyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GiftyServiceImpl implements GiftyService {

    @Autowired
//    private final SdkToken sdkToken;
    private final SdkResponseRepo sdkResponseRepo;
    private final UserInfoRepository userInfoRepository;
    private final Response response;
    private final PasswordManagerRepo passwordManagerRepo;
    private final UserDataRepository userDataRepo;
    private final LoginAttemptService loginAttemptService;
//    private String generatedSDKToken;


    @Override
    public Response createSDKToken(MobileNoDTO mobileNoDTO) {



        log.info("=>> GiftyServiceImpl:: Inside createSDKToken Method <<=");
        String mobileNo = mobileNoDTO.getMobileNo();
        log.info("mobileNo = " + mobileNo);

        String generatedSDKToken = generateSDKToken(mobileNo);
        Optional<SdkResponse> sdkResponse = sdkResponseRepo.findByMobileNo(mobileNo);

        if (sdkResponse.isPresent()) {
            sdkResponse.get().setSdkToken(generatedSDKToken);
            sdkResponseRepo.save(sdkResponse.get());
        } else {
            SdkResponse saveSdkResponseWithMobile = SdkResponse.builder()
                    .mobileNo(mobileNo)
                    .sdkToken(generatedSDKToken)
                    .build();
            sdkResponseRepo.save(saveSdkResponseWithMobile);
        }

        Response response = new Response();
        response.setStatus("SUCCESS");
        response.setStatusCode("200");
        response.setMessage("Successfully Create SDKToken!");
        response.setSdktoken(generatedSDKToken);
        response.setResponseMessage("TokenRequestDto Processed Successfully");

        // Only set the password field for SDK token response
        Optional<PasswordManager> byMobileNo = passwordManagerRepo.findByMobileNo(mobileNo);
        if (byMobileNo.isPresent() && byMobileNo.get().getPassword() != null) {
            response.setPassword(true);  // Password is set
        }else {
            response.setPassword(false);
        }

        return response;
    }


    //    The below method is for preprod Testing purposes
    @Override
    public Response createSdkTokenV2(MobileNoDTO mobileNoDTO) {
        log.info("==> GiftyServiceImpl :: inside createSdkTOken method  <==");
        String mobileNo = mobileNoDTO.getMobileNo();
        String generateSDKTokens = generateSDKToken(mobileNo);

        Optional<SdkResponse> byMobileNo = sdkResponseRepo.findByMobileNo(mobileNo);

        if (byMobileNo.isPresent()) {
            byMobileNo.get().setSdkToken(generateSDKTokens);
            sdkResponseRepo.save(byMobileNo.get());
        } else {
            SdkResponse build = SdkResponse.builder()
                    .mobileNo(mobileNo)
                    .sdkToken(generateSDKTokens)
                    .build();


            sdkResponseRepo.save(build);
        }

        Optional<PasswordManager> byMobileNo1 = passwordManagerRepo.findByMobileNo(mobileNo);

        if (byMobileNo1.isPresent() && byMobileNo1.get().getPassword() != null) {
            response.setPassword(true);
        } else {
            response.setPassword(false);
        }

        response.setStatus("SUCCESS");
        response.setStatusCode("200");
        response.setMessage("Successfully Create SDKToken!");
        response.setSdktoken(generateSDKTokens);
        response.setResponseMessage("TokenRequestDto Processed Successfully");

//        sdkToken.setMessage("SUCCESS");
//        sdkToken.setStatusCode("200");
//        sdkToken.setMessage("Successfully Create SDKToken!");
//        sdkToken.setSdkToken(generatedSDKTokens);
//


        return response;

    }

    @Override
    public Response resetPassword(ResetDTO resetDTO) {
        log.info("=>> GiftyServiceImpl:: Inside resetPassword Method <<=");

        String mobileNo = resetDTO.getMobileNo();
        log.info("mobileNo = " + mobileNo);

        String oldPassword = resetDTO.getOldPassword();


        String newPassword = resetDTO.getNewPassword();


        String confirmPassword = resetDTO.getConfirmPassword();


        Optional<PasswordManager> byMobileNoOpt = passwordManagerRepo.findByMobileNo(mobileNo);

        Response response = new Response();

        if (!newPassword.equals(confirmPassword)) {
            log.error("Please check newPassword & confirmPassword mismatch!");
            response.setStatus("FAILURE");
            response.setStatusCode("401");
            response.setMessage("Password Mismatch");
        } else if (byMobileNoOpt.isEmpty()) {
            log.error("User Not Valid");
            response.setStatus("FAILURE");
            response.setStatusCode("401");
            response.setMessage("User Not Valid");
        } else {
            PasswordManager byMobileNo = byMobileNoOpt.get();
            if (!oldPassword.equals(byMobileNo.getPassword())) {
                log.error("Invalid oldPassword");
                response.setStatus("FAILURE");
                response.setStatusCode("401");
                response.setMessage("Invalid oldPassword");
            } else {
                byMobileNo.setPassword(confirmPassword);
                passwordManagerRepo.save(byMobileNo);
                response.setStatus("SUCCESS");
                response.setStatusCode("200");
                response.setMessage("Successfully Reset Password!");
            }
        }

        return response;
    }
//
//    @Override
//    public Response loginUser(LoginDTO loginDTO) {
//        String mobileNo = loginDTO.getMobileNo();
//        String password = loginDTO.getPassword();
//
//        // Check if user is blocked
//        if (loginAttemptService.isUserBlocked(mobileNo)) {
//            return createBlockedUserResponse();
//        }
//
//        // Check if token expired
//        if (isTokenExpired(mobileNo)) {
//            return createExpiredTokenResponse();
//        }
//
//        boolean isValidLogin = validateLogin(mobileNo, password);
//
//        if (isValidLogin) {
//            loginAttemptService.resetLoginAttempts(mobileNo);
//            saveRequestDataToDatabase(loginDTO); // Save user data to the database
//            return createSuccessResponse(mobileNo);
//        } else {
//            loginAttemptService.recordLoginAttempt(mobileNo);
//            return createFailedLoginResponse(mobileNo, loginAttemptService);
//        }
//    }
//
//    private boolean isTokenExpired(String mobileNo) {
//        // Add your logic to check if token is expired for the user
//        return false; // Placeholder logic
//    }
//
//    private Response createBlockedUserResponse() {
//        Response response = new Response();
//        response.setStatus("FAILURE");
//        response.setStatusCode("403");
//        response.setMessage("You are blocked due to multiple failed login attempts. Please try again after 10 minutes.");
//        return response;
//    }
//
//    private Response createExpiredTokenResponse() {
//        Response response = new Response();
//        response.setResponseType("E");
//        response.setErrorMessage("Invalid JWT Token, Validation Fails!!");
//        response.setStatus("FAILURE");
//        response.setStatusCode("401");
//        return response;
//    }
//
//    private Response createFailedLoginResponse(String mobileNo, LoginAttemptService loginAttemptService) {
//        Response response = new Response();
//        int remainingAttempts = loginAttemptService.getMaxAttempts() - loginAttemptService.getLoginAttempts(mobileNo);
//        String message;
//        if (remainingAttempts > 0) {
//            message = "Invalid mobile number or password. You have " + remainingAttempts + " attempts left.";
//        } else {
//            message = "You are blocked due to multiple failed login attempts. Please try again after 10 minutes.";
//        }
//        response.setStatus("FAILURE");
//        response.setStatusCode("401");
//        response.setMessage(message);
//        return response;
//    }
//
//    private Response createSuccessResponse(String mobileNo) {
//        Response response = new Response();
//        response.setStatus("SUCCESS");
//        response.setStatusCode("200");
//        response.setMessage("Successfully logged in!");
//        response.setSdktoken(generateSDKToken(mobileNo)); // Changed from sdkToken to setSdkToken
//        response.setResponseMessage("TokenRequestDTO processed successfully"); // Ensure this field is correctly set
//        return response;
//    }
//
//    private void saveRequestDataToDatabase(LoginDTO loginDTO) {
//        String mobileNo = loginDTO.getMobileNo();
//        String password = loginDTO.getPassword();
//
//        // Check if the user already exists in the signIn table
//        Optional<UserData> existingSignInData = userDataRepo.findByMobileNo(mobileNo);
//
//        if (existingSignInData.isPresent()) {
//            // User already exists in the signIn table, update the record
//            existingSignInData.get().setPassword(password);
//            // You can update other fields as needed
//            userDataRepo.save(existingSignInData.get());
//
//            log.info("Data overridden for mobileNo: " + mobileNo + " in signIn table");
//        } else {
//            // User doesn't exist in the signIn table, save a new entry
//            UserData signInData = new UserData(mobileNo, password);
//            // Set other fields as needed
//            userDataRepo.save(signInData);
//            log.info("New data saved for mobileNo: " + mobileNo + " in signIn table");
//        }
//    }
//
//    private boolean validateLogin(String mobileNo, String password) {
//        // Implement your validation logic here
//        // For example, you can check if the mobileNo and password match records in the database
//        List<PasswordManager> byMobileNo = passwordManagerRepo.findByMobileNoAndPassword(mobileNo, password);
//        return !byMobileNo.isEmpty();
//    }

    @Override
    public Response loginUser(LoginDTO loginDTO) {
        String mobileNo = loginDTO.getMobileNo();
        String password = loginDTO.getPassword();



        // Check if user is blocked
        if (loginAttemptService.isUserBlocked(mobileNo)) {
            LocalDateTime blockEndTime = loginAttemptService.getBlockStartTime(mobileNo).plusMinutes(loginAttemptService.getBlockDurationMinutes());
            if (blockEndTime.isAfter(LocalDateTime.now())) {
                // User is still blocked
                return createAlreadyBlockedResponse();
            } else {
                // Unblock the user since the block duration has passed
                loginAttemptService.unblockUser(mobileNo);
            }
        }

        // Check if token expired
        if (isTokenExpired(mobileNo)) {
            return createExpiredTokenResponse();
        }

        boolean isValidLogin = validateLogin(mobileNo, password);

        if (isValidLogin) {
            loginAttemptService.resetLoginAttempts(mobileNo);
            saveRequestDataToDatabase(loginDTO); // Save user data to the database
            return createSuccessResponse(mobileNo);
        } else {
            loginAttemptService.recordLoginAttempt(mobileNo);
            return createFailedLoginResponse(mobileNo, loginAttemptService);
        }
    }

    private Response createAlreadyBlockedResponse() {
        Response response = new Response();
        response.setStatus("FAILURE");
        response.setStatusCode("403");
        response.setMessage("You are already blocked. Please try again after some time.");
        return response;
    }

    private boolean isTokenExpired(String mobileNo) {
        // Add your logic to check if token is expired for the user
        return false; // Placeholder logic
    }

    private Response createBlockedUserResponse() {
        Response response = new Response();
        response.setStatus("FAILURE");
        response.setStatusCode("403");
        response.setMessage("You are blocked due to multiple failed login attempts. Please try again after 10 minutes.");
        return response;
    }

    private Response createExpiredTokenResponse() {
        Response response = new Response();
        response.setResponseType("E");
        response.setErrorMessage("Invalid JWT Token, Validation Fails!!");
        response.setStatus("FAILURE");
        response.setStatusCode("401");
        return response;
    }

    private Response createFailedLoginResponse(String mobileNo, LoginAttemptService loginAttemptService) {
        Response response = new Response();
        int remainingAttempts = loginAttemptService.getMaxAttempts() - loginAttemptService.getLoginAttempts(mobileNo);
        String message;
        if (remainingAttempts > 0) {
            message = "Invalid mobile number or password. You have " + remainingAttempts + " attempts left.";
        } else {
            message = "You are blocked due to multiple failed login attempts. Please try again after 10 minutes.";
        }
        response.setStatus("FAILURE");
        response.setStatusCode("401");
        response.setMessage(message);
        return response;
    }

    private Response createSuccessResponse(String mobileNo) {
        Response response = new Response();
        response.setStatus("SUCCESS");
        response.setStatusCode("200");
        response.setMessage("Successfully logged in!");
        response.setSdktoken(generateSDKToken(mobileNo)); // Changed from sdkToken to setSdkToken
        response.setResponseMessage("TokenRequestDTO processed successfully"); // Ensure this field is correctly set
        return response;
    }

    private void saveRequestDataToDatabase(LoginDTO loginDTO) {
        String mobileNo = loginDTO.getMobileNo();
        String password = loginDTO.getPassword();

        // Check if the user already exists in the signIn table
        Optional<UserData> existingSignInData = userDataRepo.findByMobileNo(mobileNo);

        if (existingSignInData.isPresent()) {
            // User already exists in the signIn table, update the record
            existingSignInData.get().setPassword(password);
            // You can update other fields as needed
            userDataRepo.save(existingSignInData.get());

            log.info("Data overridden for mobileNo: " + mobileNo + " in signIn table");
        } else {
            // User doesn't exist in the signIn table, save a new entry
            UserData signInData = new UserData(mobileNo, password);
            // Set other fields as needed
            userDataRepo.save(signInData);
            log.info("New data saved for mobileNo: " + mobileNo + " in signIn table");
        }
    }

    private boolean validateLogin(String mobileNo, String password) {
        // Implement your validation logic here
        // For example, you can check if the mobileNo and password match records in the database
        List<PasswordManager> byMobileNo = passwordManagerRepo.findByMobileNoAndPassword(mobileNo, password);
        return !byMobileNo.isEmpty();
    }

    @Override
    public Response setPassword(SetPasswordDTO setPasswordDTO) {
        log.info("=>> GiftyServiceImpl:: Inside setPassword Method <<=");

        String mobileNo = setPasswordDTO.getMobileNo();
        log.info("mobileNo: " + mobileNo);

        String appPass1 = setPasswordDTO.getAppPass1();
        log.info("appPass1: " + appPass1);

        String appPass2 = setPasswordDTO.getAppPass2();
        log.info("appPass2: " + appPass2);

        Response response = new Response();

        // Check if appPass1 and appPass2 are empty or null
        if (appPass1 == null || appPass1.isEmpty() || appPass2 == null || appPass2.isEmpty()) {
            response.setStatus("FAILURE");
            response.setStatusCode("401");
            response.setMessage("Please enter a password.");
            response.setResponseMessage("TokenRequestDTO Processed Successfully");
            return response;
        }

        // Check if appPass1 and appPass2 match
        if (!appPass1.equals(appPass2)) {
            response.setStatus("FAILURE");
            response.setStatusCode("401");
            response.setMessage("Passwords do not match.");
            response.setResponseMessage("TokenRequestDTO Processed Successfully");
            return response;
        }

        Optional<SdkResponse> byMobileNo = sdkResponseRepo.findByMobileNo(mobileNo);

        if (byMobileNo.isEmpty()) {
            response.setStatus("FAILURE");
            response.setStatusCode("401");
            response.setMessage("Mobile number not found.");
            response.setResponseMessage("TokenRequestDTO Processed Successfully");
        } else if (byMobileNo.isPresent()) {
            Optional<PasswordManager> passwordManager = passwordManagerRepo.findByMobileNo(mobileNo);

            if (passwordManager.isPresent()) {
                passwordManager.get().setPassword(appPass1);
                passwordManagerRepo.save(passwordManager.get());
            } else {
                PasswordManager pManager = PasswordManager.builder()
                        .mobileNo(mobileNo)
                        .password(appPass1)
                        .build();

                passwordManagerRepo.save(pManager);
            }

            response.setStatus("SUCCESS");
            response.setStatusCode("200");
            response.setMessage("Password Set Successfully!");
        }

        return response;
    }

    private String generateSDKToken(String mobileNo) {
        // Implement JWT token generation logic here
        String key = mobileNo + new Date().getTime();
        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final byte[] hash = digest.digest(key.getBytes("UTF-8"));
            final StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                final String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.substring(0, 15);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

//    private String generateSDKTokens(String mobileNo) {
//        // Implement JWT token generation logic here
//        String key = mobileNo + new Date().getTime();
//        try {
//            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
//            final byte[] hash = digest.digest(key.getBytes("UTF-8"));
//            final StringBuilder hexString = new StringBuilder();
//            for (byte b : hash) {
//                final String hex = Integer.toHexString(0xff & b);
//                if (hex.length() == 1)
//                    hexString.append('0');
//                hexString.append(hex);
//            }
//            return hexString.substring(0, 15);
//        } catch (Exception ex) {
//            throw new RuntimeException(ex);
//        }
//    }
}