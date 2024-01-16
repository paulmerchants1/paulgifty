package com.auth.services;


import com.auth.dto.LoginDTO;
import com.auth.dto.ResetDTO;
import com.auth.dto.Response;
import com.auth.dto.SetPasswordDTO;
import com.auth.dto.sdkdto.MobileNoDTO;
import com.auth.dto.sdkdto.SdkToken;
import com.auth.entity.PasswordManager;
import com.auth.entity.UserData;
import com.auth.entity.skdentity.SdkResponse;
import com.auth.repo.PasswordManagerRepo;
import com.auth.repo.SdkResponseRepo;
import com.auth.repo.UserDataRepository;
import com.auth.repo.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GiftyServiceImpl implements GiftyService {

    @Autowired
    private final SdkToken sdkToken;
    private final SdkResponseRepo sdkResponseRepo;
    private final UserInfoRepository userInfoRepository;
    private final Response response;
    private final PasswordManagerRepo passwordManagerRepo;
    private final UserDataRepository userDataRepo;


    @Override
    public Response createSDKToken(MobileNoDTO mobileNoDTO) {

        log.info("=>> GiftyServiceImpl:: Inside createSDKToken Method <<=");
        String mobileNo = mobileNoDTO.getMobileNo();
        log.info("mobileNo = " + mobileNo);

        String generatedSDKToken = generateSDKToken(mobileNo);
        Optional<SdkResponse> sdkResponse= sdkResponseRepo.findByMobileNo(mobileNo);

        if(sdkResponse.isPresent()) {

            sdkResponse.get().setSdkToken(generatedSDKToken);
            sdkResponseRepo.save(sdkResponse.get());

        }else{

            SdkResponse saveSdkResponseWithMobile = SdkResponse.builder()
                    .mobileNo(mobileNo)
                    .sdkToken(generatedSDKToken)
                    .build();

            sdkResponseRepo.save(saveSdkResponseWithMobile);

        }
        Optional<PasswordManager> byMobileNo = passwordManagerRepo.findByMobileNo(mobileNo);

        if (byMobileNo.isPresent() && byMobileNo.get().getPassword() != null) {
            sdkToken.setPassword(true);  // Password is set
        } else {
            sdkToken.setPassword(false);  // Password is not set
        }

        sdkToken.setMessage("SUCCESS");
        sdkToken.setStatusCode("200");
        sdkToken.setMessage("Successfully Create SDKToken!");
        sdkToken.setSdkToken(generatedSDKToken);


        return sdkToken;

    }

    @Override
    public Response resetPassword(ResetDTO resetDTO) {
        log.info("=>> GiftyServiceImpl:: Inside resetPassword Method <<=");

        String mobileNo = resetDTO.getMobileNo();
        log.info("mobileNo = " + mobileNo);

        String oldPassword = resetDTO.getOldPassword();
        log.info("oldPassword = " + oldPassword);

        String newPassword = resetDTO.getNewPassword();
        log.info("newPassword = " + newPassword);

        String confirmPassword = resetDTO.getConfirmPassword();
        log.info("confirmPassword = " + confirmPassword);

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

//    @Override
//    public Response loginUser(LoginDTO loginDTO) {
//        log.info("=>> GiftyServiceImpl:: Inside loginUser Method <<=");
//
//        String mobileNo = loginDTO.getMobileNo();
//        log.info("mobileNo = " + mobileNo);
//
//        String password = loginDTO.getPassword();
//        log.info("password = " + password);
//
//        List<PasswordManager> byMobileNo = passwordManagerRepo.findByMobileNoAndPassword(mobileNo, password);
//
//        Response response = new Response();
//
//        if (byMobileNo.isEmpty()) {
//            log.error("Invalid mobileNo or password");
//            response.setStatus("FAILURE");  // Set status to 'FAILURE' for an invalid login
//            response.setStatusCode("401");  // You can use a suitable error code
//            response.setMessage("Invalid mobileNo or password");
//            response.setSdktoken(null);  // Set the token to null
//        } else {
//            String sdkToken1 = null;
//            Optional<SdkResponse> byMobileNo1 = sdkResponseRepo.findByMobileNo(mobileNo);
//            if (byMobileNo1.isPresent()) {
//                String token = generateSDKToken(mobileNo);
//                byMobileNo1.get().setSdkToken(token);
//                sdkResponseRepo.save(byMobileNo1.get());
//                sdkToken1 = token;
//            }
//            response.setStatus("SUCCESS");
//            response.setStatusCode("200");
//            response.setMessage("Successfully Login!");
//            response.setSdktoken(sdkToken1);
//        }
//
//        return response;
//    }

    @Override
    public Response loginUser(LoginDTO loginDTO) {
        log.info("=>> GiftyServiceImpl:: Inside loginUser Method <<=");

        String mobileNo = loginDTO.getMobileNo();
        log.info("mobileNo = " + mobileNo);

        String password = loginDTO.getPassword();
        log.info("password = " + password);

        // Save the request data (mobile number and password) into the database
        saveRequestDataToDatabase(loginDTO);

        Response response = new Response();

        if (validateLogin(mobileNo, password)) {
            String sdkToken1 = null;
            Optional<SdkResponse> byMobileNo1 = sdkResponseRepo.findByMobileNo(mobileNo);
            if (byMobileNo1.isPresent()) {
                String token = generateSDKToken(mobileNo);
                byMobileNo1.get().setSdkToken(token);
                sdkResponseRepo.save(byMobileNo1.get());
                sdkToken1 = token;
            }
            response.setStatus("SUCCESS");
            response.setStatusCode("200");
            response.setMessage("Successfully Login!");
            response.setSdktoken(sdkToken1);
        } else {
            log.error("Invalid mobileNo or password");
            response.setStatus("FAILURE");
            response.setStatusCode("401");
            response.setMessage("Invalid mobileNo or password");
            response.setSdktoken(null);
        }

        return response;
    }
    private boolean validateLogin(String mobileNo, String password) {
        // Implement your validation logic here
        // For example, you can check if the mobileNo and password match records in the database
        List<PasswordManager> byMobileNo = passwordManagerRepo.findByMobileNoAndPassword(mobileNo, password);
        return !byMobileNo.isEmpty();
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
}
