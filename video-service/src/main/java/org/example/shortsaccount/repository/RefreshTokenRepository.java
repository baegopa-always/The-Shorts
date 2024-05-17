package org.example.shortsaccount.repository;

import org.example.shortsaccount.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByMemberId(Long member_Id);
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}