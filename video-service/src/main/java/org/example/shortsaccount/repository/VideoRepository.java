package org.example.shortsaccount.repository;

import org.example.shortsaccount.domain.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Long> {
}