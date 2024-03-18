package com.auth.controller;


import com.auth.dto.Response;
import com.auth.dto.authdto.JwtAuthRequest;
import com.auth.dto.registoruserdto.RegisterRequestDTO;
import com.auth.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private final AuthService authService;



    @PostMapping("/register")
    public ResponseEntity<Response> signUp(@RequestBody RegisterRequestDTO userDTO) throws Exception {

        LOGGER.info("=>>AuthController:: Inside singUp Method<<=");
        Response response = authService.signUp(userDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @PostMapping("/login")
    public ResponseEntity<Response> singIn(@RequestBody JwtAuthRequest userCredentials) {

        LOGGER.info("=>>AuthController:: Inside SignIn Method<<=");
        Response response = authService.signIn(userCredentials);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
    @PostMapping("/loginv2")
    public ResponseEntity<Response> singInV2(@RequestBody JwtAuthRequest userCredentials) {

        LOGGER.info("=>>AuthController:: Inside SignIn Method<<=");
        Response response = authService.singInV2(userCredentials);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

}

