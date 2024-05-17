package com.auth.services.impl;

import com.auth.constant.AuthenticationFilterConstant;
import com.auth.entity.Token;
import com.auth.entity.skdentity.SdkResponse;
import com.auth.repo.SdkResponseRepo;
import com.auth.repo.TokenRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogoutServiceImpl implements LogoutHandler {

    private final TokenRepo tokenRepo;
    private final SdkResponseRepo sdkResponseRepo;
    private final ObjectMapper objectMapper;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) {

        final String requestToken = request.getHeader(AuthenticationFilterConstant.HEADER_AUTHORIZATION);

        if (requestToken == null || !requestToken.startsWith(AuthenticationFilterConstant.BEARER_TOKEN_START)) {
            log.warn("Token is missing or not in the expected format");
            return;
        }
        String token = requestToken.substring(7);

        String mobileNo = extractMobileNoFromRequestBody(request);

        if (mobileNo == null) {
            log.warn("Mobile number is missing in the request body");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Token storedToken = tokenRepo.findByToken(token).orElse(null);
        SdkResponse storedSdkResponse = sdkResponseRepo.findByMobileNo(mobileNo).orElse(null);

        if (storedSdkResponse == null) {
            log.warn("Mobile number {} does not exist in SdkResponse", mobileNo);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setHeader("Content-Type", "application/json");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            try {
                writeResponse(response, "FAILURE", "404", "MobileNo does not exist");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }

        if (storedToken != null) {
            log.info("Performing logout action for user with mobile number: {}", mobileNo);

            // Set the logout timestamp
            long utcTimestampMillis = System.currentTimeMillis();
            Instant instant = Instant.ofEpochMilli(utcTimestampMillis);
            ZonedDateTime utcDateTime = ZonedDateTime.ofInstant(instant, ZoneId.of("UTC"));
            ZonedDateTime istDateTime = utcDateTime.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a");
            String formattedDate = istDateTime.format(formatter);

            // Save the logout timestamp
            storedToken.setLogoutTimestamp(formattedDate);

            // Set the token as expired and revoked
            storedToken.setExpired(true);
            storedToken.setRevoked(true);

            // Save the updated Token entity for the logout event
            tokenRepo.save(storedToken);

            response.setStatus(HttpServletResponse.SC_OK);
            response.setHeader("Content-Type", "application/json");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            try {
                writeResponse(response, "SUCCESS", "200", "User logged out successfully");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            log.info("User with mobile number {} logged out successfully", mobileNo);
        }

    }
    private String extractMobileNoFromRequestBody(HttpServletRequest request) {
        try (BufferedReader reader = request.getReader()) {
            StringBuilder body = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }

            // Parse the request body to extract mobileNo
            // You can use your JSON parsing library here
            return objectMapper.readTree(body.toString()).get("mobileNo").asText();
        } catch (IOException e) {
            log.error("Error extracting mobileNo from request body: {}", e.getMessage());
            return null;
        }
    }

//



    public static void writeResponse(HttpServletResponse response, String status, String statusCode, String message) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();



        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", status);
        responseBody.put("statusCode", statusCode);
        responseBody.put("message", message);

        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
    }
}


