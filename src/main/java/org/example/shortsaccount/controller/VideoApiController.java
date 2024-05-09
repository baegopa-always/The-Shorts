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
        // video id에 해당하는 video_views count 증가
        videoService.checkVideo(id);
        // body 에서 user 정보 가져와서 사용자 재생 기록 확인
        PlayHistory playHistory;
        try {
            playHistory = playHistoryService.findFirstByUserIdAndVideoIdOrderByPlayDateDesc(request.getMemberId(), id);
            // 가장 최근 재생 기록에서 비디오 크기만큼 봤을 경우 0으로 생성해서 반환
            if (playHistory.getLastWatchTime() >= videoService.getLength(id)) {
                playHistory = playHistoryService.save(request, videoService.getLength(id));
            }
        } catch (IllegalArgumentException e) {
            playHistory = playHistoryService.save(request, videoService.getLength(id));
            // 0으로 보내주자
        }
        // 재생 시점 있을 경우 불러와서 재생
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(playHistory);
    }

    @PostMapping("/api/videos/stop/{id}")
    public ResponseEntity<PlayHistory> stopVideo(@PathVariable int id, @RequestBody PlayHistoryDTO request) {
        // play history 생성
        request.setVideoId(id);
        // 해당 video id에서 재생시간만큼 playback time 증가, ad_views 증가
        int maxSize = videoService.getLength(id);
        if (request.getPlayTime() > maxSize) {
            request.setPlayTime(maxSize);
        }

        videoService.addPlayTime(request.getVideoId(), request.getPlayTime());
        videoAdvertisementService.checkVideoAdvertisement(id, request.getPlayTime());
        // 사용자 기록 업데이트
        PlayHistory playHistory = playHistoryService.updateLastWatchTime(request); // 여기가 업데이트 였음
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