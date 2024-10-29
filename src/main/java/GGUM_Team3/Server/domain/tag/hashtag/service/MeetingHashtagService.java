package GGUM_Team3.Server.domain.tag.hashtag.service;

import GGUM_Team3.Server.domain.tag.hashtag.dto.HashtagMeetingsDTO;
import GGUM_Team3.Server.domain.tag.hashtag.dto.SearchForMeetingWithHashtagsDTO;
import GGUM_Team3.Server.domain.tag.hashtag.entity.HashtagEntity;
import GGUM_Team3.Server.domain.tag.hashtag.entity.MeetingHashtagEntity;
import GGUM_Team3.Server.domain.tag.hashtag.repository.MeetingHashtagRepository;
import GGUM_Team3.Server.domain.meeting.dto.MeetingDTO;
import GGUM_Team3.Server.domain.meeting.entity.Meeting;
import GGUM_Team3.Server.domain.meeting.repository.MeetingRepository;
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
    private final MeetingRepository meetingRepository;

    public MeetingHashtagService(MeetingHashtagRepository meetingHashtagRepository, HashtagService hashtagService, MeetingRepository meetingRepository) {
        this.meetingHashtagRepository = meetingHashtagRepository;
        this.hashtagService = hashtagService;
        this.meetingRepository = meetingRepository;
    }

    public ResponseEntity<MeetingDTO> addHashtagsToMeeting(Meeting meeting, List<String> newHashtags) {
        try {
            List<String> existingHashtagNames = meeting.getMeetingHashtagEntities().stream()
                    .map(meetingHashtagEntity -> meetingHashtagEntity.getHashtagEntity().getHashtagName())
                    .collect(Collectors.toList());

            List<String> hashtagsToAdd = newHashtags.stream()
                    .filter(hashtag -> !existingHashtagNames.contains(hashtag))
                    .collect(Collectors.toList());

            hashtagsToAdd.forEach(hashtagName -> {
                HashtagEntity hashtagEntity = hashtagService.createOrGetHashtag(hashtagName);
                MeetingHashtagEntity meetingHashtagEntity = new MeetingHashtagEntity();
                meetingHashtagEntity.setMeeting(meeting);
                meetingHashtagEntity.setHashtagEntity(hashtagEntity);
                meeting.getMeetingHashtagEntities().add(meetingHashtagEntity);
            });

            meetingRepository.save(meeting);

            MeetingDTO responseDTO = MeetingDTO.fromEntity(meeting);
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

    public ResponseEntity<Meeting> updateHashtagsForMeeting(Meeting meeting, List<String> newHashtags) {
        try {
            List<MeetingHashtagEntity> existingHashtags = meetingHashtagRepository.findByMeeting(meeting);
            meetingHashtagRepository.deleteAll(existingHashtags);

            newHashtags.forEach(hashtagName -> {
                HashtagEntity hashtagEntity = hashtagService.createOrGetHashtag(hashtagName);
                MeetingHashtagEntity meetingHashtagEntity = new MeetingHashtagEntity();
                meetingHashtagEntity.setMeeting(meeting);
                meetingHashtagEntity.setHashtagEntity(hashtagEntity);
                meetingHashtagRepository.save(meetingHashtagEntity);
            });

            meeting.setMeetingHashtagEntities(
                    meetingHashtagRepository.findByMeeting(meeting)
            );

            return new ResponseEntity<>(meeting, HttpStatus.OK);
        } catch (Exception e) {
            log.error("미팅 해시태그 수정 중 오류가 발생했습니다: {}", e.getMessage(), e);
            throw new RuntimeException("미팅 해시태그 수정 중 오류가 발생했습니다.", e);
        }
    }

    public HashtagMeetingsDTO findMeetingsByHashtag(String hashtagName) {
        List<Meeting> meetings = meetingHashtagRepository.findByHashtagEntity_HashtagName(hashtagName)
                .stream()
                .map(MeetingHashtagEntity::getMeeting)
                .distinct()
                .collect(Collectors.toList());

        if (meetings.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해시태그 '" + hashtagName + "'에 해당하는 모임이 없습니다.");
        }

        List<SearchForMeetingWithHashtagsDTO> searchResults = meetings.stream()
                .map(meeting -> new SearchForMeetingWithHashtagsDTO(
                        meeting.getId(), // 이거 수정햇음 meeting 은 string형식의 uuid임
                        meeting.getTitle()
                ))
                .collect(Collectors.toList());

        return new HashtagMeetingsDTO(hashtagName, searchResults);
    }
}
