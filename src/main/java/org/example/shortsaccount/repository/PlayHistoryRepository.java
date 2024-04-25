package org.example.shortsaccount.repository;

import org.example.shortsaccount.domain.PlayHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayHistoryRepository extends JpaRepository<PlayHistory, Long> {
}