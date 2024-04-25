package org.example.shortsaccount.controller;

import lombok.RequiredArgsConstructor;
import org.example.shortsaccount.domain.PlayHistory;
import org.example.shortsaccount.domain.Video;
import org.example.shortsaccount.dto.AddPlayHistoryRequest;
import org.example.shortsaccount.dto.AddVideoRequest;
import org.example.shortsaccount.dto.UpdateVideoRequest;
import org.example.shortsaccount.dto.VideoResponse;
import org.example.shortsaccount.service.PlayHistoryService;
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
    @PostMapping("/api/videos")
    public ResponseEntity<Video> addVideo(@RequestBody AddVideoRequest request) {
        Video savedVideo = videoService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedVideo);
    }

    @PostMapping("/api/play/{id}")
    public ResponseEntity<Video> playVideo(@PathVariable long id) {
        // video id에 해당하는 video_views count 증가
        Video savedVideo = videoService.checkVideo(id);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedVideo);
    }

    @PostMapping("/api/stop/{id}")
    public ResponseEntity<PlayHistory> stopVideo(@PathVariable int id, @RequestBody AddPlayHistoryRequest request) {
        // play history 생성
        request.setVideoId(id);
        PlayHistory playHistory = playHistoryService.save(request);
        // 해당 video id에서 재생시간만큼 playback time 증가, ad_views 증가
        Video video = videoService.addPlayTime(id, request.getPlayTime());
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
        // video_id 에 해당하는 statistic 에서 조회수 증가 ??
        return ResponseEntity.ok()
                .body(new VideoResponse(video));
    }

    @DeleteMapping("/api/videos/{id}")
    public ResponseEntity<Void> deleteVideo(@PathVariable long id) {
        videoService.delete(id);
        return ResponseEntity.ok()
                .build();
    }

    @PutMapping("/api/articles/{id}")
    public ResponseEntity<Video> updateVideo(@PathVariable long id,
                                                 @RequestBody UpdateVideoRequest request) {
        Video updatedVideo = videoService.update(id, request);
        return ResponseEntity.ok()
                .body(updatedVideo);
    }
}