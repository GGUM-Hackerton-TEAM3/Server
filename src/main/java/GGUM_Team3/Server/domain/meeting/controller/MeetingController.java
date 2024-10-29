package GGUM_Team3.Server.domain.meeting.controller;

import GGUM_Team3.Server.domain.meeting.service.MeetingService;
import GGUM_Team3.Server.global.sercurity.TokenProvider;
import GGUM_Team3.Server.domain.meeting.dto.MeetingDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meetings")
@Tag(name = "모임", description = "모임 생성, 조회, 갱신, 참여 및 탈퇴 관련 API")
public class MeetingController {

    private final MeetingService meetingService;
    private final TokenProvider tokenProvider;

    @PostMapping("/create")
    public ResponseEntity<?> createMeeting(
            @RequestPart MeetingDTO meetingDTO,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
        try {
            MeetingDTO createdMeeting = meetingService.createMeeting(meetingDTO, imageFile);
            return ResponseEntity.ok(createdMeeting);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating meeting: " + e.getMessage());
        }
    }

    @Operation(summary = "모임 갱신", description = "모임 ID로 특정 모임 정보를 갱신합니다.")
    @PutMapping("/{id}/update")
    public ResponseEntity<MeetingDTO> updateMeeting(
            @PathVariable String id,
            @RequestBody MeetingDTO meetingDTO) {
        MeetingDTO updatedMeeting = meetingService.updateMeeting(id, meetingDTO);
        return ResponseEntity.ok(updatedMeeting);
    }

    @Operation(summary = "사용자 참여 모임 조회", description = "사용자가 참여 중인 모든 모임 목록을 조회합니다.")
    @GetMapping("/my-meetings")
    public ResponseEntity<List<MeetingDTO>> getMyMeetings(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String userId = tokenProvider.validateAndGetUserId(token);
        List<MeetingDTO> myMeetings = meetingService.getMeetingsByUser(userId);
        return ResponseEntity.ok(myMeetings);
    }

    // 기존 코드
    @Operation(summary = "모임 검색", description = "키워드로 모임을 검색합니다.")
    @GetMapping("/search")
    public ResponseEntity<List<MeetingDTO>> searchMeetings(@RequestParam String keyword) {
        return ResponseEntity.ok(meetingService.searchMeetings(keyword));
    }

    @Operation(summary = "모임 ID 검색", description = "모임 제목으로 모임 ID를 검색합니다.")
    @GetMapping("/search-id")
    public ResponseEntity<String> searchMeetingIdByTitle(@RequestParam String title) {
        String meetingId = meetingService.getMeetingIdByTitle(title);
        return ResponseEntity.ok(meetingId);
    }

    @Operation(summary = "모임 조회", description = "모임 ID로 모임 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<MeetingDTO> getMeetingById(@PathVariable String id) {
        return ResponseEntity.ok(meetingService.getMeetingById(id));
    }

    @Operation(summary = "모임 참여", description = "모임 ID와 사용자 토큰으로 모임에 참여합니다.")
    @PostMapping("/{id}/join")
    public ResponseEntity<MeetingDTO> joinMeetingById(@PathVariable String id, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String userId = tokenProvider.validateAndGetUserId(token);
        return ResponseEntity.ok(meetingService.joinMeeting(id, userId));
    }

    @Operation(summary = "모임 탈퇴", description = "모임 ID와 사용자 토큰으로 모임에서 탈퇴합니다.")
    @PostMapping("/{id}/leave")
    public ResponseEntity<MeetingDTO> leaveMeetingById(@PathVariable String id, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String userId = tokenProvider.validateAndGetUserId(token);
        return ResponseEntity.ok(meetingService.leaveMeeting(id, userId));
    }

    // 전체 모임 조회 API
    @Operation(summary = "전체 모임 조회", description = "모든 모임 목록을 조회합니다.")
    @GetMapping("/all")
    public ResponseEntity<List<MeetingDTO>> getAllMeetings() {
        List<MeetingDTO> allMeetings = meetingService.getAllMeetings();
        return ResponseEntity.ok(allMeetings);
    }
}
