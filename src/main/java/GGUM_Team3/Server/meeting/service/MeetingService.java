// 모임 서비스
package GGUM_Team3.Server.meeting.service;

import GGUM_Team3.Server.tag.category.entity.CategoryEntity;
import GGUM_Team3.Server.tag.category.repository.CategoryRepository;
import GGUM_Team3.Server.tag.hashtag.entity.HashtagEntity;
import GGUM_Team3.Server.tag.hashtag.entity.MeetingHashtagEntity;
import GGUM_Team3.Server.tag.hashtag.repository.HashtagRepository;
import GGUM_Team3.Server.tag.hashtag.repository.MeetingHashtagRepository;
import GGUM_Team3.Server.tag.hashtag.service.MeetingHashtagService;
import GGUM_Team3.Server.domain.user.entity.UserEntity;
import GGUM_Team3.Server.domain.user.service.UserService;
import GGUM_Team3.Server.meeting.DTO.MeetingDTO;
import GGUM_Team3.Server.meeting.entity.Meeting;
import GGUM_Team3.Server.meeting.repository.MeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private CategoryRepository categoryRepository;


    public MeetingDTO createMeeting(MeetingDTO meetingDTO) throws BindException {
        if (meetingDTO.getTitle() == null || meetingDTO.getTitle().isEmpty()) {
            BindException bindException = new BindException(meetingDTO, "meetingDTO");
            bindException.rejectValue("title", "NotEmpty", "Meeting title cannot be null or empty");
            throw bindException;
        }
        if (meetingRepository.findByTitle(meetingDTO.getTitle()).isPresent()) {
            throw new IllegalArgumentException("Meeting title already exists");
        }

        // categoryId를 사용하여 카테고리 엔티티 조회
        CategoryEntity category = categoryRepository.findById(meetingDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID: " + meetingDTO.getCategoryId()));

        Meeting meeting;
        try {
            meeting = meetingRepository.saveAndFlush(
                    Meeting.builder()
                            .title(meetingDTO.getTitle())
                            .creatorId(meetingDTO.getCreatorId()) // creatorId도 받아와야 한다.
                            .description(meetingDTO.getDescription())
                            .maxParticipants(meetingDTO.getMaxParticipants())
                            .startTime(meetingDTO.getStartTime())
                            .region(meetingDTO.getRegion())
                            .notice(meetingDTO.getNotice())
                            .chatRoomId(meetingDTO.getChatRoomId())
                            .category(category)
                            .build()
            );
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Data integrity violation: " + e.getMessage());
        } catch (ConstraintViolationException e) {
            throw new IllegalArgumentException("Constraint violation: " + e.getMessage());
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
        meetingRepository.save(meeting);

        return  MeetingDTO.fromEntity(meeting);
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


    // title에서 keyword 있는지 확인
    public List<MeetingDTO> searchMeetings(String keyword) {
        // keyword의 앞뒤 공백 제거 및 대소문자 무시 검색 적용
        return meetingRepository.findByTitleContainingIgnoreCase(keyword.trim()).stream()
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

