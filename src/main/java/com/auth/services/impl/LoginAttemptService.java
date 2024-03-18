package com.auth.services.impl;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class LoginAttemptService {

    private final Map<String, Integer> loginAttempts = new HashMap<>();
    private final int maxAttempts = 4;
    private final int blockDurationMinutes = 10; // Block duration in minutes
    private final Map<String, LocalDateTime> blockedUsers = new HashMap<>();

    public void recordLoginAttempt(String username) {
        int attempts = loginAttempts.getOrDefault(username, 0) + 1;
        loginAttempts.put(username, attempts);
        if (attempts >= maxAttempts) {
            blockedUsers.put(username, LocalDateTime.now().plusMinutes(blockDurationMinutes));
        }
    }

    public boolean isUserBlocked(String username) {
        return blockedUsers.containsKey(username) && blockedUsers.get(username).isAfter(LocalDateTime.now());
    }

    public void resetLoginAttempts(String username) {
        loginAttempts.remove(username);
        blockedUsers.remove(username);
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public int getLoginAttempts(String username) {
        return loginAttempts.getOrDefault(username, 0);
    }
}
