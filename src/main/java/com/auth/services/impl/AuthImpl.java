package com.auth.services.impl;



import com.auth.Util.SecureFiles;
import com.auth.constant.AuthenticationAuthorizationConstant;
import com.auth.constant.RoleConstants;
import com.auth.dto.Response;
import com.auth.dto.authdto.JwtAuthRequest;
import com.auth.dto.authdto.JwtAuthResponse;
import com.auth.dto.registoruserdto.RegisterRequestDTO;
import com.auth.dto.registoruserdto.UserResponseDTO;
import com.auth.entity.Role;
import com.auth.entity.Token;
import com.auth.entity.TokenType;
import com.auth.entity.User;
import com.auth.repo.RoleRepo;
import com.auth.repo.TokenRepo;
import com.auth.repo.UserRepo;
import com.auth.security.JwtTokenHelper;
import com.auth.services.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthImpl implements AuthService {

    @Autowired
    private final JwtTokenHelper jwtTokenHelper;

    @Autowired
    private final UserDetailsService userDetailsService;

    @Autowired
    private final AuthenticationManager authenticationManager;


    @Autowired
    private final UserRepo userRepo;

    @Autowired
    private final RoleRepo roleRepo;

    @Autowired
    private final ModelMapper mMapper;

    @Autowired
    private final Response response;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
   private  final TokenRepo tokenRepo;

    @Override
    public Response signUp(RegisterRequestDTO userDTO) throws Exception {

        log.info("=>> RegisterUser:: Inside executeService Method<<=");


        //=>> Convert userDTO To user:
        User user = mMapper.map(userDTO, User.class);

        /*---- Now We have Encoded The Password ----*/
        user.setUserPassword(this.passwordEncoder.encode(user.getUserPassword()));

        //=> Fetching user role from RegisterRequest:
        String userRole = userDTO.getUserRole();

        //=> Set based on RoleId from the given userRole:
        Integer roleId = userRole.equalsIgnoreCase(RoleConstants.NORMAL_USER_NAME) ? RoleConstants.NORMAL_USER : RoleConstants.ADMIN_USER;

        //=> Fetch Role with the help of roleId:
        Role role = roleRepo.findById(roleId).orElseThrow(() -> new Exception("Role Not " +
                "Found With RoleId = " + roleId));

        //=> Set the role into user:
        user.getRoles().add(role);

        User saveUser;
        try {
            //=> Now Simply save the user into DB:
            saveUser = userRepo.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Please Enter Unique EmailId");
        }

        //=>> Now Again Convert saveUser to userDTO:
        UserResponseDTO registerUser = mMapper.map(saveUser, UserResponseDTO.class);


        /*----Simply Return The Response----*/
        response.setStatus(AuthenticationAuthorizationConstant.SUCCESS_STATUS);
        response.setStatusCode(AuthenticationAuthorizationConstant.STATUS_CODE);
        response.setMessage("Successfully User Register Into DB!");
        response.setData(registerUser);

        return response;
    }


    public Response signIn(JwtAuthRequest userCredentials) {
        log.info("=>>SignIn:: Inside executeService Method<<=");

//        String userName = new SecureFiles().decryptKey("fb40b261a143ae1bec691cedebf5120e", userCredentials.getUsername());
//        String password = new SecureFiles().decryptKey("fb40b261a143ae1bec691cedebf5120e", userCredentials.getPassword());
//


        /*----Now call the authenticate Method for authentication----*/
        this.authenticate(userCredentials.getUsername(),userCredentials.getPassword());

        /*----Now call the loadUserByUsername Method for Creating UserDetails----*/
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(userCredentials.getUsername());

        /*----Now Call the generateToken Method for generating the Token----*/
        String token = this.jwtTokenHelper.generateToken(userDetails);

        // Assuming you have a UTC timestamp (replace this with your actual timestamp)
        long utcTimestampMillis = System.currentTimeMillis();

        // Convert UTC timestamp to ZonedDateTime in UTC
        Instant instant = Instant.ofEpochMilli(utcTimestampMillis);
        ZonedDateTime utcDateTime = ZonedDateTime.ofInstant(instant, ZoneId.of("UTC"));

        // Convert UTC datetime to Indian Standard Time (IST)
        ZonedDateTime istDateTime = utcDateTime.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));

        // Format the IST datetime as a string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a");
        String formattedDate = istDateTime.format(formatter);

        // Print the IST timestamp
        System.out.println(formattedDate);
        // revokeAllUserTokens((User) userDetails);

        var tokenObj = Token.builder()
                .user((User) userDetails)
                .expired(false)
                .revoked(false)
                .tokenType(TokenType.BEARER)
                .token(token)
                .loginTimestamp(formattedDate)
                .build();
        tokenRepo.save(tokenObj);

        /*----Simply Return Response-----*/
        JwtAuthResponse response = new JwtAuthResponse();
        response.setStatus(AuthenticationAuthorizationConstant.SUCCESS_STATUS);
        response.setStatusCode(AuthenticationAuthorizationConstant.STATUS_CODE);
        response.setMessage("Successfully generated Token.");
        response.setToken(token);

        return response;
    }


    @Override
    public Response singInV2(JwtAuthRequest userCredentials) {
        log.info("=>>SignIn:: Inside executeService Method<<=");

        String userName = new SecureFiles().decryptKey("fb40b261a143ae1bec691cedebf5120e", userCredentials.getUsername());
        String password = new SecureFiles().decryptKey("fb40b261a143ae1bec691cedebf5120e", userCredentials.getPassword());



        /*----Now call the authenticate Method for authentication----*/
        this.authenticate(userName,password);

        /*----Now call the loadUserByUsername Method for Creating UserDetails----*/
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);

        /*----Now Call the generateToken Method for generating the Token----*/
        String token = this.jwtTokenHelper.generateToken(userDetails);

        // Assuming you have a UTC timestamp (replace this with your actual timestamp)
        long utcTimestampMillis = System.currentTimeMillis();

        // Convert UTC timestamp to ZonedDateTime in UTC
        Instant instant = Instant.ofEpochMilli(utcTimestampMillis);
        ZonedDateTime utcDateTime = ZonedDateTime.ofInstant(instant, ZoneId.of("UTC"));

        // Convert UTC datetime to Indian Standard Time (IST)
        ZonedDateTime istDateTime = utcDateTime.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));

        // Format the IST datetime as a string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a");
        String formattedDate = istDateTime.format(formatter);

        // Print the IST timestamp
        System.out.println(formattedDate);
        // revokeAllUserTokens((User) userDetails);

        var tokenObj = Token.builder()
                .user((User) userDetails)
                .expired(false)
                .revoked(false)
                .tokenType(TokenType.BEARER)
                .token(token)
                .loginTimestamp(formattedDate)
                .build();
        tokenRepo.save(tokenObj);

        /*----Simply Return Response-----*/
        JwtAuthResponse response = new JwtAuthResponse();
        response.setStatus(AuthenticationAuthorizationConstant.SUCCESS_STATUS);
        response.setStatusCode(AuthenticationAuthorizationConstant.STATUS_CODE);
        response.setMessage("Successfully generated Token.");
        response.setToken(token);

        return response;
    }


    private void authenticate(String username, String password) {

        log.info("===: SignIn:: Inside authenticate Method :===");
        UsernamePasswordAuthenticationToken authenticationToken  = new UsernamePasswordAuthenticationToken(username,
                password);
        try {
            this.authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            log.error("Invalid Details i.e UserName & Password");
            throw new BadCredentialsException("Invalid UserName or Password");
        }

    }
    private void revokeAllUserTokens(User user) {
        log.info("===: SignIn:: Inside revokeAllUserTokens Method :===");
        var validUserTokens = tokenRepo.findAllValidTokensByUser(user.getUserId());
        if (validUserTokens.isEmpty()) return;
        validUserTokens.forEach(t-> {
            t.setRevoked(true);
            t.setExpired(true);
        });
        tokenRepo.saveAll(validUserTokens);
    }
}




