package org.example.shortsaccount.controller;

import lombok.RequiredArgsConstructor;
import org.example.shortsaccount.domain.Video;
import org.example.shortsaccount.dto.AddVideoRequest;
import org.example.shortsaccount.dto.UpdateVideoRequest;
import org.example.shortsaccount.dto.VideoResponse;
import org.example.shortsaccount.service.VideoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class VideoApiController {
    private final VideoService videoService;
    @PostMapping("/api/videos")
    public ResponseEntity<Video> addVideo(@RequestBody AddVideoRequest request) {
        Video savedVideo = videoService.save(request);
        // video_id 를 가지고 statisics 도 만든다. 나머지는 0
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedVideo);
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