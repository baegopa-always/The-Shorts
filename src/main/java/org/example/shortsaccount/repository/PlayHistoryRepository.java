package org.example.shortsaccount.repository;

import org.example.shortsaccount.domain.PlayHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayHistoryRepository extends JpaRepository<PlayHistory, Long> {
    Optional<PlayHistory> findByUserId(int userId);
    Optional<PlayHistory> findByUserIdAndVideoId(int userId, int videoId);
}