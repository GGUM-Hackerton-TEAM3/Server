package GGUM_Team3.Server.tempMeeting.service;

import GGUM_Team3.Server.tempMeeting.dto.TempMeetingDTO;
import GGUM_Team3.Server.tempMeeting.entity.TempMeetingEntity;
import GGUM_Team3.Server.tempMeeting.repository.TempMeetingRepository;
import GGUM_Team3.Server.tag.hashtag.entity.HashtagEntity;
import GGUM_Team3.Server.tag.hashtag.entity.MeetingHashtagEntity;
import GGUM_Team3.Server.tag.hashtag.repository.HashtagRepository;
import GGUM_Team3.Server.tag.hashtag.repository.MeetingHashtagRepository;
import GGUM_Team3.Server.tag.hashtag.service.MeetingHashtagService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TempMeetingService {

    private final TempMeetingRepository tempMeetingRepository;

    private final HashtagRepository hashtagRepository;

    private final MeetingHashtagRepository meetingHashtagRepository;
    private final MeetingHashtagService meetingHashtagService;

    public TempMeetingService(TempMeetingRepository tempMeetingRepository, HashtagRepository hashtagRepository, MeetingHashtagRepository meetingHashtagRepository, MeetingHashtagService meetingHashtagService) {
        this.tempMeetingRepository = tempMeetingRepository;
        this.hashtagRepository = hashtagRepository;
        this.meetingHashtagRepository = meetingHashtagRepository;
        this.meetingHashtagService = meetingHashtagService;
    }

//    public TempMeetingEntity createMeeting(TempMeetingDTO tempMeetingDTO) throws BindException {
//        // 미팅 제목이 없을 경우 유효성 검사 실패 예외 발생
//        if (tempMeetingDTO.getTempMeetingTitle() == null || tempMeetingDTO.getTempMeetingTitle().isEmpty()) {
//            BindException bindException = new BindException(tempMeetingDTO, "tempMeetingDTO");
//            bindException.rejectValue("tempMeetingTitle", "NotEmpty", "Meeting title cannot be null or empty");
//            throw bindException;
//        }
//
//        // 중복 제목 체크 (리스트 크기 체크로)
//        if (!tempMeetingRepository.findByTempMeetingTitle(tempMeetingDTO.getTempMeetingTitle()).isEmpty()) {
//            throw new IllegalArgumentException("Meeting title already exists");
//        }
//
//        TempMeetingEntity tempMeeting;
//        try {
//            tempMeeting = tempMeetingRepository.saveAndFlush(
//                    TempMeetingEntity.builder()
//                            .tempMeetingTitle(tempMeetingDTO.getTempMeetingTitle())
//                            .build()
//            );
//        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
//            throw new IllegalArgumentException("Meeting title already exists");
//        }
//
//        List<MeetingHashtagEntity> meetingHashtagEntities = tempMeetingDTO
//                .getHashtags()
//                .stream()
//                .map(hashtagName -> {
//                    HashtagEntity hashtagEntity = hashtagRepository
//                            .findByHashtagName(hashtagName)
//                            .orElseGet(() -> hashtagRepository.save(new HashtagEntity(hashtagName)));
//                    return MeetingHashtagEntity.builder()
//                            .tempMeetingEntity(tempMeeting)
//                            .hashtagEntity(hashtagEntity)
//                            .build();
//                }).collect(Collectors.toList());
//
//        meetingHashtagRepository.saveAll(meetingHashtagEntities);
//
//        tempMeeting.setMeetingHashtagEntities(meetingHashtagEntities);
//
//        return tempMeetingRepository.save(tempMeeting);
//    }
//
//    public TempMeetingEntity getMeetingByTitle(String tempMeetingTitle) {
//        return tempMeetingRepository.findByTempMeetingTitle(tempMeetingTitle)
//                .stream()
//                .findFirst()
//                .orElseThrow(() -> new EntityNotFoundException("Meeting not found with title: " + tempMeetingTitle));
//    }
//
//    public TempMeetingEntity updateMeetingHashtags(TempMeetingDTO tempMeetingDTO) {
//        TempMeetingEntity meeting = getMeetingByTitle(tempMeetingDTO.getTempMeetingTitle());
//
//        // MeetingHashtagService를 통해 해시태그 업데이트
//        ResponseEntity<?> response = meetingHashtagService.updateHashtagsForMeeting(meeting, tempMeetingDTO.getHashtags());
//
//        if (response.getStatusCode() == HttpStatus.OK) {
//            return (TempMeetingEntity) response.getBody();
//        } else {
//            throw new RuntimeException("해시태그 업데이트 중 오류가 발생했습니다.");
//        }
//    }

}