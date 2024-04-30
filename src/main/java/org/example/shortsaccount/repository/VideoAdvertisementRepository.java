package org.example.shortsaccount.repository;

import org.example.shortsaccount.domain.VideoAdvertisement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VideoAdvertisementRepository extends JpaRepository<VideoAdvertisement, Long> {
}
