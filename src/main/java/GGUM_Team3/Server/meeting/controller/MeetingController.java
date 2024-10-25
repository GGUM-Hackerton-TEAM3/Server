package GGUM_Team3.Server.meeting.controller;

import GGUM_Team3.Server.meeting.DTO.MeetingDTO;
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

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public ResponseEntity<MeetingDTO> createMeeting(@RequestBody MeetingDTO meetingDTO) {
        return ResponseEntity.ok(meetingService.createMeeting(meetingDTO));
    }

    @GetMapping("/search")
    public ResponseEntity<List<MeetingDTO>> searchMeetings(@RequestParam String keyword) {
        return ResponseEntity.ok(meetingService.searchMeetings(keyword));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeetingDTO> getMeetingById(@PathVariable UUID id) {
        return ResponseEntity.ok(meetingService.getMeetingById(id));
    }

    @GetMapping
    public ResponseEntity<List<MeetingDTO>> getAllMeetings() {
        return ResponseEntity.ok(meetingService.getAllMeetings());
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/join")
    public ResponseEntity<MeetingDTO> joinMeeting(@PathVariable UUID id, @RequestParam UUID userId) {
        return ResponseEntity.ok(meetingService.joinMeeting(id, userId));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/leave")
    public ResponseEntity<MeetingDTO> leaveMeeting(@PathVariable UUID id, @RequestParam UUID userId) {
        return ResponseEntity.ok(meetingService.leaveMeeting(id, userId));
    }
}
