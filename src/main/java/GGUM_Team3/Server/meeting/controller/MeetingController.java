package GGUM_Team3.Server.meeting.controller;

import GGUM_Team3.Server.meeting.DTO.MeetingDTO;
import GGUM_Team3.Server.meeting.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/meetings")
public class MeetingController {
    @Autowired
    private MeetingService meetingService;

    //@PreAuthorize("isAuthenticated()")
    // 테스트 완료
    @PostMapping("/create")
    public ResponseEntity<?> createMeeting(@RequestBody MeetingDTO meetingDTO) {
        try {
            MeetingDTO createdMeeting = meetingService.createMeeting(meetingDTO);
            return ResponseEntity.ok(createdMeeting);
        } catch (BindException e) {
            return ResponseEntity.badRequest().body("Meeting title cannot be null or empty.");
        }
    }

    // 테스트 완료
    @GetMapping("/search")
    public ResponseEntity<List<MeetingDTO>> searchMeetings(@RequestParam String keyword) {
        // URL 디코딩을 적용하여 공백 처리 가능하도록 수정
        try {
            keyword = URLDecoder.decode(keyword, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(meetingService.searchMeetings(keyword));
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
}
