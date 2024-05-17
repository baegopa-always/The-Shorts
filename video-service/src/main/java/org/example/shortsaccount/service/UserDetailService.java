//package org.example.shortsaccount.service;
//
//import lombok.RequiredArgsConstructor;
//
//import org.example.shortsaccount.domain.Member;
//import org.example.shortsaccount.repository.UserRepository;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.stereotype.Service;
//
//@RequiredArgsConstructor
//@Service
//public class UserDetailService implements UserDetailsService {
//    private final UserRepository userRepository;
//    @Override
//    public Member loadUserByUsername(String email) {
//        return userRepository.findByEmail(email)
//                .orElseThrow(() -> new IllegalArgumentException((email)));
//    }
//}