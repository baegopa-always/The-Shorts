package org.example.shortsaccount.service;

import lombok.RequiredArgsConstructor;
import org.example.shortsaccount.domain.Member;
import org.example.shortsaccount.dto.UserDTO;
import org.example.shortsaccount.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    public Long save(UserDTO dto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return userRepository.save(Member.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .role(dto.getRole())
                .build()).getId();
    }

    public Member findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    public Member findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user - email"));
    }

    @Override
    public Member loadUserByUsername(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException((email)));
    }
}