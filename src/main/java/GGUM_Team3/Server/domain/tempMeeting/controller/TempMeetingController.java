package GGUM_Team3.Server.domain.tempMeeting.controller;

import GGUM_Team3.Server.domain.tag.hashtag.service.MeetingHashtagService;
import GGUM_Team3.Server.domain.tempMeeting.dto.TempMeetingDTO;
import GGUM_Team3.Server.domain.tempMeeting.entity.TempMeetingEntity;
import GGUM_Team3.Server.domain.tempMeeting.service.TempMeetingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bungae/meetings")
public class TempMeetingController {

    private final TempMeetingService tempMeetingService;
    private final MeetingHashtagService meetingHashtagService;

    public TempMeetingController(TempMeetingService tempMeetingService, MeetingHashtagService meetingHashtagService) {
        this.tempMeetingService = tempMeetingService;
        this.meetingHashtagService = meetingHashtagService;
    }

    @PostMapping("/create")
    public ResponseEntity<TempMeetingEntity> createMeeting(@RequestBody TempMeetingDTO tempMeetingDTO) throws BindException {

        try {
            TempMeetingEntity tempMeeting = tempMeetingService.createMeeting(tempMeetingDTO);
            return ResponseEntity.ok(tempMeeting);
        } catch (BindException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

//    @PutMapping("/addHashtags")
//    public ResponseEntity<?> addHashtagsToMeeting(@RequestBody TempMeetingDTO tempMeetingDTO) {
//        try {
//            // TempMeetingEntity 객체를 가져온다.
//            TempMeetingEntity meeting = tempMeetingService.getMeetingByTitle(tempMeetingDTO.getTempMeetingTitle());
//
//            // 해시태그 리스트를 TempMeetingDTO에서 가져온다.
//            List<String> hashtags = tempMeetingDTO.getHashtags();
//
//            // MeetingHashtagService를 이용해 해시태그들을 미팅에 추가한다.
//            TempMeetingEntity updatedMeeting = meetingHashtagService.updateHashtagsForMeeting(meeting, hashtags).getBody();
//
//            // 업데이트된 미팅을 DTO로 변환하여 반환
//            TempMeetingDTO responseDTO = new TempMeetingDTO();
//            responseDTO.setTempMeetingId(updatedMeeting.getTempMeetingId());
//            responseDTO.setTempMeetingTitle(updatedMeeting.getTempMeetingTitle());
//
//            List<String> updatedHashtags = updatedMeeting.getMeetingHashtagEntities().stream()
//                    .map(meetingHashtagEntity -> meetingHashtagEntity.getHashtagEntity().getHashtagName())
//                    .collect(Collectors.toList());
//            responseDTO.setHashtags(updatedHashtags);
//
//            return ResponseEntity.ok(responseDTO);
//        } catch (Exception e) {
//            throw new RuntimeException("미팅에 해시태그 추가 중 오류가 발생했습니다.", e);
//        }
//    }
}
