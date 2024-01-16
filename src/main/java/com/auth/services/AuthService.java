package com.auth.services;


import com.auth.dto.Response;
import com.auth.dto.authdto.JwtAuthRequest;
import com.auth.dto.registoruserdto.RegisterRequestDTO;

public interface AuthService {

    Response signUp(RegisterRequestDTO userDTO) throws Exception;


    Response signIn(JwtAuthRequest userCredentials);

}
