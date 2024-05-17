package com.auth.repo;

import com.auth.entity.PasswordManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
public interface PasswordManagerRepo extends JpaRepository<PasswordManager, Long> {
    Optional<PasswordManager> findByMobileNo(String mobileNo);

    Optional<PasswordManager> findByMobileNoAndPassword(String mobileNo, String password);
}
