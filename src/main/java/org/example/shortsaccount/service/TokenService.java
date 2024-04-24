package org.example.shortsaccount.service;

import lombok.RequiredArgsConstructor;
import org.example.shortsaccount.config.jwt.TokenProvider;
import org.example.shortsaccount.domain.Member;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    public String createNewAccessToken(String refreshToken) {
        if(!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getMemberId();
        Member member = userService.findById(userId);

        return tokenProvider.generateToken(member, Duration.ofHours(2));
    }
}