package org.example.shortsaccount.controller;

import lombok.RequiredArgsConstructor;
import org.example.shortsaccount.domain.PlayHistory;
import org.example.shortsaccount.domain.UserHistory;
import org.example.shortsaccount.domain.Video;
import org.example.shortsaccount.dto.PlayHistoryDTO;
import org.example.shortsaccount.dto.VideoDTO;
import org.example.shortsaccount.dto.VideoResponse;
import org.example.shortsaccount.service.PlayHistoryService;
import org.example.shortsaccount.service.UserHistoryService;
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
    private final UserHistoryService userHistoryService;

    @PostMapping("/api/videos")
    public ResponseEntity<Video> addVideo(@RequestBody VideoDTO request) {
        Video savedVideo = videoService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedVideo);
    }

    @PostMapping("/api/videos/play/{id}")
    public ResponseEntity<UserHistory> playVideo(@PathVariable int id, @RequestBody PlayHistoryDTO request) {
        // video id에 해당하는 video_views count 증가
        videoService.checkVideo(id);
        // body 에서 user 정보 가져와서 사용자 재생 기록 확인
        UserHistory userHistory;
        try {
            userHistory = userHistoryService.findVideoHistoryByUserId(request.getMemberId(), id);
        } catch (IllegalArgumentException e) {
            userHistory = userHistoryService.save(id, request);
            // 0으로 보내주자
        }
        // 재생 시점 있을 경우 불러와서 재생
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userHistory);
    }

    @PostMapping("/api/videos/stop/{id}")
    public ResponseEntity<PlayHistory> stopVideo(@PathVariable int id, @RequestBody PlayHistoryDTO request) {
        // play history 생성
        request.setVideoId(id);
        PlayHistory playHistory = playHistoryService.save(request);
        // 해당 video id에서 재생시간만큼 playback time 증가, ad_views 증가
        videoService.addPlayTime(id, request.getPlayTime());
        videoService.addAdViews(id, request.getPlayTime());
        // 사용자 기록 업데이트
        userHistoryService.updateLastWatchTime(id, request);
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