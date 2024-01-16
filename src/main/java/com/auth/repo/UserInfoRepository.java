package com.auth.repo;


import com.auth.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    boolean existsByMobileNoOrCardNo(String mobileNo, String cardNo);
    UserInfo findByToken(String token);

    UserInfo findByMobileNoAndToken(String mobileNo, String token);
    List<UserInfo> findByMobileNo(String mobileNo);

    UserInfo findByMobileNoOrCardNo(String mobileNo, String cardNo);

    UserInfo findByMobileNoAndCardNo(String mobileNo, String cardNo);

    UserInfo findByCardNo(String cardNo);
    List<UserInfo> findByMobileNoAndPassword(String mobileNo, String password);
}

