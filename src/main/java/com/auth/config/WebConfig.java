package com.auth.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Bean
    public FilterRegistrationBean<XSSProtectionHeaderFilter> xssProtectionHeaderFilter() {
        FilterRegistrationBean<XSSProtectionHeaderFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new XSSProtectionHeaderFilter());
        registrationBean.addUrlPatterns("/*"); // Apply this filter to all requests
        return registrationBean;
    }
}
