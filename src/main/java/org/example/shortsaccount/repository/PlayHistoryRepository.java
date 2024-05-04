package org.example.shortsaccount.repository;

import org.example.shortsaccount.domain.PlayHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PlayHistoryRepository extends JpaRepository<PlayHistory, Long> {
    Optional<PlayHistory> findByUserId(int userId);
    Optional<PlayHistory> findByUserIdAndVideoId(int userId, int videoId);
    Optional<PlayHistory> findFirstByUserIdAndVideoIdOrderByPlayDateDesc(int userId, int videoId);
    Optional<List<PlayHistory>> findAllByVideoIdAndPlayDateBetween(int videoId, LocalDateTime from, LocalDateTime to);
}