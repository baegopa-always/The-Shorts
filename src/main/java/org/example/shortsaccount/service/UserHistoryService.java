package org.example.shortsaccount.service;

import lombok.RequiredArgsConstructor;
import org.example.shortsaccount.domain.UserHistory;
import org.example.shortsaccount.dto.PlayHistoryDTO;
import org.example.shortsaccount.repository.UserHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserHistoryService {
    private final UserHistoryRepository userHistoryRepository;

    public UserHistory save(int id, PlayHistoryDTO request) {
        return userHistoryRepository.save(UserHistory.builder()
                .videoId(id)
                .userId(request.getMemberId())
                .lastWatchTime(request.getPlayTime())
                .build());
    }

    @Transactional
    public UserHistory updateLastWatchTime(long id, PlayHistoryDTO request) {
        UserHistory userHistory = userHistoryRepository.findByUserIdAndVideoId(request.getMemberId(), (int) id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
        userHistory.update(request.getPlayTime());
        return userHistory;
    }

    public UserHistory findAllVideoHistoryByUserId(long id) {
        return userHistoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
    }

    public UserHistory findVideoHistoryByUserId(int userId, int videoId) {
        return userHistoryRepository.findByUserIdAndVideoId(userId, videoId)
                .orElseThrow(() -> new IllegalArgumentException("not found user: " + userId + " video: "+ videoId));
    }

}
