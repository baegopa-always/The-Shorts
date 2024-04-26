package org.example.shortsaccount.repository;

import org.example.shortsaccount.domain.UserHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserHistoryRepository extends JpaRepository<UserHistory, Long> {
    Optional<UserHistory> findByUserId(int userId);
    Optional<UserHistory> findByUserIdAndVideoId(int userId, int videoId);
}
