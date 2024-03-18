package com.auth.constant;

public class  SecurityConfigConstants {

    /*----Adding private constructor to hide the implicit public one ----*/
    private SecurityConfigConstants() {}

    public static final String[] PUBLIC_URL = {
            "/api/user/login/**",
            "/api/user/loginv2/**",
            "/api/user/sdk-token/**",
            "/api/user/register/**",
            "/api/user/verify/**",
           "/api/user/privacy-policy/**",
            "/api/user/terms-conditions/**",
            "/api/user/policy/**"

    };
}