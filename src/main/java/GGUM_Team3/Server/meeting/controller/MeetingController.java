package GGUM_Team3.Server.meeting.controller;

import GGUM_Team3.Server.meeting.DTO.MeetingDTO;
import GGUM_Team3.Server.meeting.service.LikeService;
import GGUM_Team3.Server.meeting.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/meetings")
public class MeetingController {
    @Autowired
    private MeetingService meetingService;

    @Autowired
    private LikeService likeService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public ResponseEntity<MeetingDTO> createMeeting(@RequestBody MeetingDTO meetingDTO) {
        return ResponseEntity.ok(meetingService.createMeeting(meetingDTO));
    }

    @GetMapping("/search")
    public ResponseEntity<List<MeetingDTO>> searchMeetings(@RequestParam String keyword) {
        return ResponseEntity.ok(meetingService.searchMeetings(keyword));
    }

    // 카테고리별 모임 조회
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<MeetingDTO>> getMeetingsByCategory(@PathVariable String categoryId) {
        return ResponseEntity.ok(meetingService.getMeetingsByCategory(categoryId));
    }

    // 지역별 모임 조회
    @GetMapping("/region/{region}")
    public ResponseEntity<List<MeetingDTO>> getMeetingsByRegion(@PathVariable String region) {
        return ResponseEntity.ok(meetingService.getMeetingsByRegion(region));
    }

    // 태그로 모임 조회
    @GetMapping("/tag/{tag}")
    public ResponseEntity<List<MeetingDTO>> getMeetingsByTag(@PathVariable String tag) {
        return ResponseEntity.ok(meetingService.getMeetingsByTag(tag));
    }

    // 좋아요가 많은 순으로 모임 조회
    @GetMapping("/top-liked")
    public ResponseEntity<List<MeetingDTO>> getTopLikedMeetings() {
        return ResponseEntity.ok(meetingService.getTopLikedMeetings());
    }

    // 특정 사용자가 좋아요한 모임 조회
    @GetMapping("/liked-by-user/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<MeetingDTO>> getMeetingsLikedByUser(@PathVariable String userId) {
        return ResponseEntity.ok(meetingService.getMeetingsLikedByUser(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeetingDTO> getMeetingById(@PathVariable String id) {
        return ResponseEntity.ok(meetingService.getMeetingById(id));
    }

    @GetMapping
    public ResponseEntity<List<MeetingDTO>> getAllMeetings() {
        return ResponseEntity.ok(meetingService.getAllMeetings());
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/join")
    public ResponseEntity<MeetingDTO> joinMeeting(@PathVariable String id, @RequestParam String userId) {
        return ResponseEntity.ok(meetingService.joinMeeting(id, userId));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/leave")
    public ResponseEntity<MeetingDTO> leaveMeeting(@PathVariable String id, @RequestParam String userId) {
        return ResponseEntity.ok(meetingService.leaveMeeting(id, userId));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/like")
    public ResponseEntity<Integer> likeMeeting(@PathVariable String id, @RequestParam String userId) {
        int likesCount = likeService.likeMeeting(id, userId);
        return ResponseEntity.ok(likesCount);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/unlike")
    public ResponseEntity<Integer> unlikeMeeting(@PathVariable String id, @RequestParam String userId) {
        int likesCount = likeService.unlikeMeeting(id, userId);
        return ResponseEntity.ok(likesCount);
    }
}