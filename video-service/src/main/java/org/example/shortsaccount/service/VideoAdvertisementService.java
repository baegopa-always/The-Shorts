package org.example.shortsaccount.service;

import lombok.RequiredArgsConstructor;
import org.example.shortsaccount.domain.Video;
import org.example.shortsaccount.domain.VideoAdvertisement;
import org.example.shortsaccount.repository.AdvertisementRepository;
import org.example.shortsaccount.repository.VideoAdvertisementRepository;
import org.example.shortsaccount.repository.VideoRepository;
import org.example.shortsaccount.util.RandomNumberGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class VideoAdvertisementService {
    private final VideoAdvertisementRepository videoAdvertisementRepository;
    private final AdvertisementRepository advertisementRepository;
    private final VideoRepository videoRepository;

    @Transactional
    public void checkVideoAdvertisement(int videoId, int playtime) {
        try {
            videoRepository.findById((long) videoId)
                    .orElseThrow(() -> new IllegalArgumentException("not found: " + videoId));
            RandomNumberGenerator randomNumberGenerator = new RandomNumberGenerator();
            int count = playtime / 300;
            for (int i = 0; i < count; i++) {
                int maxSize = advertisementRepository.countAllBy();
                videoAdvertisementRepository.save(VideoAdvertisement.builder()
                        .videoId(videoId)
                        .adId(randomNumberGenerator.randomNumberGenerator(maxSize))
                        .build());
            }
        } catch (IllegalArgumentException e) {
            return;
        }

    }

}
