package GGUM_Team3.Server.domain.tag.hashtag.controller;

import GGUM_Team3.Server.domain.tag.hashtag.dto.HashtagMeetingsDTO;
import GGUM_Team3.Server.domain.tag.hashtag.service.HashtagService;
import GGUM_Team3.Server.domain.tag.hashtag.service.MeetingHashtagService;
import GGUM_Team3.Server.domain.tempMeeting.dto.TempMeetingDTO;
import GGUM_Team3.Server.domain.tempMeeting.entity.TempMeetingEntity;
import GGUM_Team3.Server.domain.tempMeeting.service.TempMeetingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bungae/hashtags")
@RequiredArgsConstructor
public class HashtagController {

    private final HashtagService hashtagService;
    private final TempMeetingService tempMeetingService;
    private final MeetingHashtagService meetingHashtagService;

    @GetMapping("/search")
    public HashtagMeetingsDTO searchMeetingsByHashtag(@RequestParam String hashtag) {
        return meetingHashtagService.findMeetingsByHashtag(hashtag);
    }

    @PutMapping("/updateHashtags")
    public ResponseEntity<?> updateHashtagsForMeeting(@RequestBody TempMeetingDTO tempMeetingDTO) {
        try {
            // 서비스 계층을 통해 해시태그 업데이트 수행
            TempMeetingEntity updatedMeeting = tempMeetingService.updateMeetingHashtags(tempMeetingDTO);
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
    public ResponseEntity<?> addHashtagsToMeeting(@RequestBody TempMeetingDTO tempMeetingDTO) {
        try {
            // TempMeetingEntity 객체를 가져온다.
            TempMeetingEntity meeting = tempMeetingService.getMeetingByTitle(tempMeetingDTO.getTempMeetingTitle());

            // 해시태그 리스트를 TempMeetingDTO에서 가져온다.
            List<String> hashtags = tempMeetingDTO.getHashtags();

            // MeetingHashtagService를 이용해 해시태그들을 미팅에 추가한다.
            ResponseEntity<TempMeetingDTO> addMeetingResponse = meetingHashtagService.addHashtagsToMeeting(meeting, hashtags);

            return addMeetingResponse; // 그대로 반환
        } catch (Exception e) {
            throw new RuntimeException("미팅에 해시태그 추가 중 오류가 발생했습니다.", e);
        }
    }
}
