package com.auth.security;

import com.auth.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepo userRepo; // Assuming you have a UserRepo for user email-based authentication


    @Override
    public UserDetails loadUserByUsername (String userName) throws UsernameNotFoundException {

        log.info("===: CustomUserDetailService:: Inside loadUserByUsername Method :===");

        log.info("userName = " + userName);

        /*----loading user from database by username----*/
        return this.userRepo.findByUserEmail(userName).orElseThrow(() -> new UsernameNotFoundException("User not " +
                "found With userEmail = " + userName));


    }



}
