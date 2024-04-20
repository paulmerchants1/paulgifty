package com.auth.services.impl;


import com.auth.services.TestService;
import org.springframework.stereotype.Service;

@Service
public class TestImpl implements TestService {
    @Override
    public String welcome() {
        return "Welcome To PaulMerchants";
    }
}
