// 모임 서비스
package GGUM_Team3.Server.meeting.service;

import GGUM_Team3.Server.domain.tag.hashtag.entity.HashtagEntity;
import GGUM_Team3.Server.domain.tag.hashtag.entity.MeetingHashtagEntity;
import GGUM_Team3.Server.domain.tag.hashtag.repository.HashtagRepository;
import GGUM_Team3.Server.domain.tag.hashtag.repository.MeetingHashtagRepository;
import GGUM_Team3.Server.domain.tag.hashtag.service.MeetingHashtagService;
import GGUM_Team3.Server.domain.user.entity.UserEntity;
import GGUM_Team3.Server.domain.user.service.UserService;
import GGUM_Team3.Server.meeting.DTO.MeetingDTO;
import GGUM_Team3.Server.meeting.entity.Meeting;
import GGUM_Team3.Server.meeting.repository.MeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MeetingService {
    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MeetingHashtagService meetingHashtagService;

    @Autowired
    private HashtagRepository hashtagRepository;

    @Autowired
    private MeetingHashtagRepository meetingHashtagRepository;


    public Meeting createMeeting(MeetingDTO meetingDTO) throws BindException {
        if (meetingDTO.getTitle() == null || meetingDTO.getTitle().isEmpty()) {
            BindException bindException = new BindException(meetingDTO, "meetingDTO");
            bindException.rejectValue("title", "NotEmpty", "Meeting title cannot be null or empty");
            throw bindException;
        }

        if (!meetingRepository.findByTitleContaining(meetingDTO.getTitle()).isEmpty()) {
            throw new IllegalArgumentException("Meeting title already exists");
        }

        Meeting meeting;
        try {
            meeting = meetingRepository.saveAndFlush(
                    Meeting.builder()
                            .title(meetingDTO.getTitle())
                            .description(meetingDTO.getDescription())
                            .maxParticipants(meetingDTO.getMaxParticipants())
                            .startTime(meetingDTO.getStartTime())
                            .region(meetingDTO.getRegion())
                            .notice(meetingDTO.getNotice())
                            .chatRoomId(meetingDTO.getChatRoomId())
                            .categoryId(meetingDTO.getCategoryId())
                            .build()
            );
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            throw new IllegalArgumentException("Meeting title already exists");
        }

        List<MeetingHashtagEntity> meetingHashtagEntities = meetingDTO
                .getHashtags()
                .stream()
                .map(hashtagName -> {
                    HashtagEntity hashtagEntity = hashtagRepository
                            .findByHashtagName(hashtagName)
                            .orElseGet(() -> hashtagRepository.save(new HashtagEntity(hashtagName)));
                    return MeetingHashtagEntity.builder()
                            .meeting(meeting)
                            .hashtagEntity(hashtagEntity)
                            .build();
                }).collect(Collectors.toList());

        meetingHashtagRepository.saveAll(meetingHashtagEntities);
        meeting.setMeetingHashtagEntities(meetingHashtagEntities);

        return meetingRepository.save(meeting);
    }

    public List<MeetingDTO> getAllMeetings() {
        return meetingRepository.findAll().stream()
                .map(MeetingDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public MeetingDTO getMeetingById(String id) {
        return meetingRepository.findById(id)
                .map(MeetingDTO::fromEntity)
                .orElseThrow(() -> new RuntimeException("Meeting not found"));
    }

    public Meeting getMeetingByTitle(String title) {
        return meetingRepository.findByTitleContaining(title)
                .stream()
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Meeting not found with title: " + title));
    }

    public Meeting updateMeetingHashtags(MeetingDTO meetingDTO) {
        Meeting meeting = getMeetingByTitle(meetingDTO.getTitle());

        ResponseEntity<?> response = meetingHashtagService.updateHashtagsForMeeting(meeting, meetingDTO.getHashtags());

        if (response.getStatusCode() == HttpStatus.OK) {
            return (Meeting) response.getBody();
        } else {
            throw new RuntimeException("해시태그 업데이트 중 오류가 발생했습니다.");
        }
    }


    //title에서 keyword 있는지 확인
    public List<MeetingDTO> searchMeetings(String keyword) {
        return meetingRepository.findByTitleContaining(keyword).stream()
                .map(MeetingDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public MeetingDTO joinMeeting(String meetingId, String userId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new RuntimeException("Meeting not found"));
        UserEntity user = userService.getById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (meeting.getParticipants().size() < meeting.getMaxParticipants()) {
            meeting.getParticipants().add(user);
            Meeting updatedMeeting = meetingRepository.save(meeting);
            return MeetingDTO.fromEntity(updatedMeeting);
        }
        throw new RuntimeException("Meeting is full.");
    }

    public MeetingDTO leaveMeeting(String meetingId, String userId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new RuntimeException("Meeting not found"));
        UserEntity user = userService.getById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        meeting.getParticipants().remove(user);
        Meeting updatedMeeting = meetingRepository.save(meeting);
        return MeetingDTO.fromEntity(updatedMeeting);
    }
}

