package com.auth.repo;


import com.auth.entity.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDataRepository extends JpaRepository<UserData, String> {
    Optional<UserData> findByMobileNo(String mobileNo);
}
