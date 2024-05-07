package com.auth.services.impl;//package com.auth.services.impl;

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
        if (blockedUsers.containsKey(username)) {
            LocalDateTime blockEndTime = blockedUsers.get(username);
            if (blockEndTime.isAfter(LocalDateTime.now())) {
                // User is still blocked
                return true;
            } else {
                // Unblock the user since the block duration has passed
                blockedUsers.remove(username);
                loginAttempts.remove(username);
            }
        }
        return false;
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

    public boolean shouldBlockUser(String username) {
        return getLoginAttempts(username) >= getMaxAttempts();
    }

    public LocalDateTime getBlockStartTime(String username) {
        return blockedUsers.get(username);
    }

    public void unblockUser(String username) {
        blockedUsers.remove(username);
    }

    public void blockUser(String username) {
        blockedUsers.put(username, LocalDateTime.now().plusMinutes(blockDurationMinutes));
    }

    public boolean hasExceededMaxAttempts(String username) {
        return getLoginAttempts(username) >= getMaxAttempts();
    }

    // Method to record the start time when a user is blocked
    public void recordBlockStartTime(String username) {
        blockedUsers.put(username, LocalDateTime.now());
    }

    public int  getBlockDurationMinutes() {
        return blockDurationMinutes;
    }
}


