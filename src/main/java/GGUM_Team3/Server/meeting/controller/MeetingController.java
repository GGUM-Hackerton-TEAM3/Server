package GGUM_Team3.Server.meeting.controller;

import GGUM_Team3.Server.meeting.DTO.MeetingDTO;
import GGUM_Team3.Server.meeting.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/meetings")
public class MeetingController {
    @Autowired
    private MeetingService meetingService;

    //@PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public ResponseEntity<?> createMeeting(@RequestBody MeetingDTO meetingDTO) {
        try {
            MeetingDTO createdMeeting = meetingService.createMeeting(meetingDTO);
            return ResponseEntity.ok(createdMeeting);
        } catch (BindException e) {
            return ResponseEntity.badRequest().body("Meeting title cannot be null or empty.");
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<MeetingDTO>> searchMeetings(@RequestParam String keyword) {
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
