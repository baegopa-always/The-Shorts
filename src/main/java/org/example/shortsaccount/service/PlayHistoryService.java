package org.example.shortsaccount.service;

import lombok.RequiredArgsConstructor;
import org.example.shortsaccount.domain.PlayHistory;
import org.example.shortsaccount.dto.PlayHistoryDTO;
import org.example.shortsaccount.repository.PlayHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PlayHistoryService {
    private final PlayHistoryRepository playHistoryRepository;

    public PlayHistory save(PlayHistoryDTO request) {
        return playHistoryRepository.save(PlayHistory.builder()
                .videoId(request.getVideoId())
                .userId(request.getMemberId())
                .lastWatchTime(request.getPlayTime())
                .build());
    }

    @Transactional
    public PlayHistory updateLastWatchTime(PlayHistoryDTO request) {
        PlayHistory playHistory = playHistoryRepository.findByUserIdAndVideoId(request.getMemberId(), request.getVideoId())
                .orElseThrow(() -> new IllegalArgumentException("not found: " + request.getVideoId()));
        playHistory.update(request.getPlayTime());
        return playHistory;
    }

    public PlayHistory findAllVideoHistoryByUserId(long userId) {
        return playHistoryRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + userId));
    }

    public PlayHistory findVideoHistoryByUserId(int userId, int videoId) {
        return playHistoryRepository.findByUserIdAndVideoId(userId, videoId)
                .orElseThrow(() -> new IllegalArgumentException("not found user: " + userId + " video: "+ videoId));
    }
}
