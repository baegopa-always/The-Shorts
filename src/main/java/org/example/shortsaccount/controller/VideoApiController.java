package org.example.shortsaccount.controller;

import lombok.RequiredArgsConstructor;
import org.example.shortsaccount.domain.PlayHistory;
import org.example.shortsaccount.domain.Video;
import org.example.shortsaccount.dto.PlayHistoryDTO;
import org.example.shortsaccount.dto.VideoDTO;
import org.example.shortsaccount.dto.VideoResponse;
import org.example.shortsaccount.service.PlayHistoryService;
import org.example.shortsaccount.service.VideoAdvertisementService;
import org.example.shortsaccount.service.VideoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class VideoApiController {
    private final VideoService videoService;
    private final PlayHistoryService playHistoryService;
    private final VideoAdvertisementService videoAdvertisementService;

    @PostMapping("/api/videos")
    public ResponseEntity<Video> addVideo(@RequestBody VideoDTO request) {
        Video savedVideo = videoService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedVideo);
    }

    @PostMapping("/api/videos/play/{id}")
    public ResponseEntity<PlayHistory> playVideo(@PathVariable int id, @RequestBody PlayHistoryDTO request) {
        request.setVideoId(id);
        videoService.checkVideo(id);
        PlayHistory playHistory;
        try {
            playHistory = playHistoryService.findFirstByUserIdAndVideoIdOrderByPlayDateDesc(request.getMemberId(), id);
            if (playHistory.getLastWatchTime() >= videoService.getLength(id)) {
                playHistory = playHistoryService.save(request, videoService.getLength(id));
            }
        } catch (IllegalArgumentException e) {
            playHistory = playHistoryService.save(request, videoService.getLength(id));
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(playHistory);
    }

    @PostMapping("/api/videos/stop/{id}")
    public ResponseEntity<PlayHistory> stopVideo(@PathVariable int id, @RequestBody PlayHistoryDTO request) {
        request.setVideoId(id);
        int maxSize = videoService.getLength(id);
        if (request.getPlayTime() > maxSize) {
            request.setPlayTime(maxSize);
        }
        videoService.addPlayTime(request.getVideoId(), request.getPlayTime());
        videoAdvertisementService.checkVideoAdvertisement(id, request.getPlayTime());
        PlayHistory playHistory = playHistoryService.updateLastWatchTime(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(playHistory);
    }

    @GetMapping("/api/videos")
    public ResponseEntity<List<VideoResponse>> findAllVideos() {
        List<VideoResponse> videos = videoService.findAll()
                .stream()
                .map(VideoResponse::new)
                .toList();
        return ResponseEntity.ok()
                .body(videos);
    }

    @GetMapping("/api/videos/{id}")
    public ResponseEntity<VideoResponse> findVideo(@PathVariable long id) {
        Video video = videoService.findById(id);
        return ResponseEntity.ok()
                .body(new VideoResponse(video));
    }

    @DeleteMapping("/api/videos/{id}")
    public ResponseEntity<Void> deleteVideo(@PathVariable long id) {
        videoService.delete(id);
        return ResponseEntity.ok()
                .build();
    }

    @PutMapping("/api/videos/{id}")
    public ResponseEntity<Video> updateVideo(@PathVariable long id,
                                                 @RequestBody VideoDTO request) {
        Video updatedVideo = videoService.update(id, request);
        return ResponseEntity.ok()
                .body(updatedVideo);
    }
}