package org.example.shortsaccount.repository;

import org.example.shortsaccount.domain.VideoAdvertisement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VideoAdvertisementRepository extends JpaRepository<VideoAdvertisement, Long> {
    Optional<List<VideoAdvertisement>> findAllByVideoIdAndAdTimestampBetween(int videoId, LocalDateTime from, LocalDateTime to);
}
