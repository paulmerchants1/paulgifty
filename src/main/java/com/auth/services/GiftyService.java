package com.auth.services;


import com.auth.dto.LoginDTO;
import com.auth.dto.ResetDTO;
import com.auth.dto.Response;
import com.auth.dto.SetPasswordDTO;
import com.auth.dto.sdkdto.MobileNoDTO;

public interface GiftyService {
    Response createSDKToken(MobileNoDTO mobileNoDTO);

    Response resetPassword(ResetDTO resetDTO);

    Response loginUser(LoginDTO loginDTO);

    Response setPassword(SetPasswordDTO setPasswordDTO);
}
