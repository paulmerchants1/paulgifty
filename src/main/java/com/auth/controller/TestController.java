package com.auth.controller;


import com.auth.services.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class TestController {

    private final TestService testService;

    @GetMapping("/welcome")
    public String welcome() {
        return testService.welcome();
    }

    @GetMapping("/login_world")
    public String home()
    {
        return "Hello World";
    }
}
