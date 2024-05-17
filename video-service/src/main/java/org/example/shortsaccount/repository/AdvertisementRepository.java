package org.example.shortsaccount.repository;

import org.example.shortsaccount.domain.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    int countAllBy();
}
