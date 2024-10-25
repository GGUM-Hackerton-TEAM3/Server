package GGUM_Team3.Server.domain.tag.hashtag.service;

import GGUM_Team3.Server.domain.tag.hashtag.dto.HashtagMeetingsDTO;
import GGUM_Team3.Server.domain.tag.hashtag.dto.SearchForMeetingWithHashtagsDTO;
import GGUM_Team3.Server.domain.tag.hashtag.entity.HashtagEntity;
import GGUM_Team3.Server.domain.tag.hashtag.entity.MeetingHashtagEntity;
import GGUM_Team3.Server.domain.tag.hashtag.repository.MeetingHashtagRepository;
import GGUM_Team3.Server.domain.tempMeeting.dto.TempMeetingDTO;
import GGUM_Team3.Server.domain.tempMeeting.entity.TempMeetingEntity;
import GGUM_Team3.Server.domain.tempMeeting.repository.TempMeetingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MeetingHashtagService {
    private final MeetingHashtagRepository meetingHashtagRepository;
    private final HashtagService hashtagService;

    private final TempMeetingRepository tempMeetingRepository;

    public MeetingHashtagService(MeetingHashtagRepository meetingHashtagRepository, HashtagService hashtagService, TempMeetingRepository tempMeetingRepository) {
        this.meetingHashtagRepository = meetingHashtagRepository;
        this.hashtagService = hashtagService;
        this.tempMeetingRepository = tempMeetingRepository;
    }

    public ResponseEntity<TempMeetingDTO> addHashtagsToMeeting(TempMeetingEntity meeting, List<String> newHashtags) {
        try {
            // 이미 존재하는 해시태그 이름들 가져오기
            List<String> existingHashtagNames = meeting.getMeetingHashtagEntities().stream()
                    .map(meetingHashtagEntity -> meetingHashtagEntity.getHashtagEntity().getHashtagName())
                    .collect(Collectors.toList());

            // 새로운 해시태그 중 기존에 없는 것만 추가
            List<String> hashtagsToAdd = newHashtags.stream()
                    .filter(hashtag -> !existingHashtagNames.contains(hashtag))
                    .collect(Collectors.toList());

            // 해시태그 추가
            hashtagsToAdd.forEach(hashtagName -> {
                HashtagEntity hashtagEntity = hashtagService.createOrGetHashtag(hashtagName);
                MeetingHashtagEntity meetingHashtagEntity = new MeetingHashtagEntity();
                meetingHashtagEntity.setTempMeetingEntity(meeting);
                meetingHashtagEntity.setHashtagEntity(hashtagEntity);
                meeting.getMeetingHashtagEntities().add(meetingHashtagEntity);  // 기존 컬렉션에 새로운 해시태그 엔티티 추가
            });

            // 변경된 미팅 엔티티를 저장
            tempMeetingRepository.save(meeting);

            // DTO로 변환하여 반환
            TempMeetingDTO responseDTO = new TempMeetingDTO();
            responseDTO.setTempMeetingId(meeting.getTempMeetingId());
            responseDTO.setTempMeetingTitle(meeting.getTempMeetingTitle());

            List<String> updatedHashtagsNames = meeting.getMeetingHashtagEntities().stream()
                    .map(meetingHashtagEntity -> meetingHashtagEntity.getHashtagEntity().getHashtagName())
                    .collect(Collectors.toList());
            responseDTO.setHashtags(updatedHashtagsNames);

            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("미팅에 해시태그 추가 중 오류가 발생했습니다: {}", e.getMessage(), e);
            throw new RuntimeException("미팅에 해시태그 추가 중 오류가 발생했습니다.", e);
        }
    }



    // 해시태그 수정 메서드
    public ResponseEntity<TempMeetingEntity> updateHashtagsForMeeting(TempMeetingEntity meeting, List<String> newHashtags) {
        try {
            // 기존 해시태그 삭제
            List<MeetingHashtagEntity> existingHashtags = meetingHashtagRepository.findByTempMeetingEntity(meeting);
            meetingHashtagRepository.deleteAll(existingHashtags);

            // 새로운 해시태그 추가
            newHashtags.forEach(hashtagName -> {
                HashtagEntity hashtagEntity = hashtagService.createOrGetHashtag(hashtagName);
                MeetingHashtagEntity meetingHashtagEntity = new MeetingHashtagEntity();
                meetingHashtagEntity.setTempMeetingEntity(meeting);
                meetingHashtagEntity.setHashtagEntity(hashtagEntity);
                meetingHashtagRepository.save(meetingHashtagEntity);
            });

            // 미팅 객체에 새로운 해시태그들 추가
            meeting.setMeetingHashtagEntities(
                    meetingHashtagRepository.findByTempMeetingEntity(meeting)
            );

            return new ResponseEntity<>(meeting, HttpStatus.OK);
        } catch (Exception e) {
            log.error("미팅 해시태그 수정 중 오류가 발생했습니다: {}", e.getMessage(), e);
            throw new RuntimeException("미팅 해시태그 수정 중 오류가 발생했습니다.", e);
        }
    }

    public HashtagMeetingsDTO findMeetingsByHashtag(String hashtagName) {
        // 해시태그 이름을 통해 관련된 MeetingHashtagEntity들을 조회
        List<TempMeetingEntity> tempMeetings = meetingHashtagRepository.findByHashtagEntity_HashtagName(hashtagName)
                .stream()
                .map(MeetingHashtagEntity::getTempMeetingEntity)
                .distinct() // 중복 제거
                .collect(Collectors.toList());

        // 검색된 미팅이 없으면 ResponseStatusException 던짐
        if (tempMeetings.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해시태그 '" + hashtagName + "'에 해당하는 모임이 없습니다.");
        }

        // TempMeetingEntity를 SearchForMeetingWithHashtagsDTO로 변환
        List<SearchForMeetingWithHashtagsDTO> searchForMeetingWithHashtagsDTOS = tempMeetings.stream()
                .map(tempMeeting -> new SearchForMeetingWithHashtagsDTO(
                        tempMeeting.getTempMeetingId(),
                        tempMeeting.getTempMeetingTitle()
                ))
                .collect(Collectors.toList());

        return new HashtagMeetingsDTO(hashtagName, searchForMeetingWithHashtagsDTOS);
    }
}
