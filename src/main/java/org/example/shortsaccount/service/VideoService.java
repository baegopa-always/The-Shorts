package org.example.shortsaccount.service;

import lombok.RequiredArgsConstructor;
import org.example.shortsaccount.domain.Video;
import org.example.shortsaccount.dto.AddVideoRequest;
import org.example.shortsaccount.dto.UpdateVideoRequest;
import org.example.shortsaccount.repository.VideoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class VideoService {
    private final VideoRepository videoRepository;

    public Video save(AddVideoRequest request) {
        return videoRepository.save(request.toEntity());
    }

    public List<Video> findAll() {
        return videoRepository.findAll();
    }

    public Video findById(long id) {
        return videoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
    }

    public void delete(long id) {
        videoRepository.deleteById(id);
    }

    @Transactional
    public Video update(long id, UpdateVideoRequest request) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
        video.update(request.getTitle(), request.getLength());
        return video;
    }

    @Transactional
    public Video checkVideo(long id) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
        video.checkVideo();
        return video;
    }

    @Transactional
    public Video addPlayTime(long id, int playTime) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
        video.addPlayTime(playTime);
        return video;
    }
}