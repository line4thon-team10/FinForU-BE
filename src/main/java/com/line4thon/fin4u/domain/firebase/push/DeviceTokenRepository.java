package com.line4thon.fin4u.domain.firebase.push;


import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Long> {
    List<DeviceToken> findByMemberIdAndActiveTrue(Long memberId);
    List<DeviceToken> findByToken(String token);
}
