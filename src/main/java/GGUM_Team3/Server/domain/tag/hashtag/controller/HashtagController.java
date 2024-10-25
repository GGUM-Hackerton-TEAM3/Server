package GGUM_Team3.Server.domain.tag.hashtag.controller;

import GGUM_Team3.Server.domain.tag.hashtag.dto.HashtagMeetingsDTO;
import GGUM_Team3.Server.domain.tag.hashtag.service.HashtagService;
import GGUM_Team3.Server.domain.tag.hashtag.service.MeetingHashtagService;
import GGUM_Team3.Server.meeting.DTO.MeetingDTO;
import GGUM_Team3.Server.meeting.entity.Meeting;
import GGUM_Team3.Server.meeting.service.MeetingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/meeting/hashtags")
@RequiredArgsConstructor
public class HashtagController {

    private final HashtagService hashtagService;
    private final MeetingService meetingService;
    private final MeetingHashtagService meetingHashtagService;

    @GetMapping("/search")
    public HashtagMeetingsDTO searchMeetingsByHashtag(@RequestParam String hashtag) {
        return meetingHashtagService.findMeetingsByHashtag(hashtag);
    }

    @PutMapping("/updateHashtags")
    public ResponseEntity<?> updateHashtagsForMeeting(@RequestBody MeetingDTO meetingDTO) {
        try {
            // 해시태그 업데이트 수행
            Meeting updatedMeeting = meetingService.updateMeetingHashtags(meetingDTO);
            return ResponseEntity.ok(updatedMeeting);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("미팅 해시태그 수정 중 오류가 발생했습니다.");
        }
    }

    @PutMapping("/addHashtags")
    public ResponseEntity<?> addHashtagsToMeeting(@RequestBody MeetingDTO meetingDTO) {
        try {
            Meeting meeting = meetingService.getMeetingByTitle(meetingDTO.getId());
            List<String> hashtags = meetingDTO.getHashtags();
            ResponseEntity<MeetingDTO> addMeetingResponse = meetingHashtagService.addHashtagsToMeeting(meeting, hashtags);

            return addMeetingResponse; // 그대로 반환
        } catch (Exception e) {
            throw new RuntimeException("미팅에 해시태그 추가 중 오류가 발생했습니다.", e);
        }
    }
}
