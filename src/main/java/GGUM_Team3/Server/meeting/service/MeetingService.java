package GGUM_Team3.Server.meeting.service;

import GGUM_Team3.Server.domain.image.service.ImageService;
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
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    private ImageService imageService;

    public MeetingDTO createMeeting(MeetingDTO meetingDTO, MultipartFile imageFile) throws BindException {
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

        // 이미지 파일을 업로드하고 URL을 가져옴
        String imageUrl = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            imageUrl = imageService.saveImage(imageFile); // S3에 저장 후 URL 반환
        }

        Meeting meeting;
        try {
            meeting = meetingRepository.saveAndFlush(
                    Meeting.builder()
                            .title(meetingDTO.getTitle())
                            .creatorId(meetingDTO.getCreatorId())
                            .description(meetingDTO.getDescription())
                            .maxParticipants(meetingDTO.getMaxParticipants())
                            .startTime(meetingDTO.getStartTime())
                            .region(meetingDTO.getRegion())
                            .notice(meetingDTO.getNotice())
                            .chatRoomId(meetingDTO.getChatRoomId())
                            .category(category)
                            .imageUrl(imageUrl) // 이미지 URL 설정
                            .build()
            );
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Data integrity violation: " + e.getMessage());
        } catch (ConstraintViolationException e) {
            throw new IllegalArgumentException("Constraint violation: " + e.getMessage());
        }

        // 해시태그 엔티티 생성 및 저장
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

        return MeetingDTO.fromEntity(meeting);
    }

    public MeetingDTO updateMeeting(String meetingId, MeetingDTO meetingDTO) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new EntityNotFoundException("Meeting not found"));

        // 업데이트할 필드 설정
        meeting.setTitle(meetingDTO.getTitle());
        meeting.setDescription(meetingDTO.getDescription());
        meeting.setMaxParticipants(meetingDTO.getMaxParticipants());
        meeting.setStartTime(meetingDTO.getStartTime());
        meeting.setRegion(meetingDTO.getRegion());
        meeting.setNotice(meetingDTO.getNotice());
        meeting.setChatRoomId(meetingDTO.getChatRoomId());

        // 카테고리 설정
        CategoryEntity category = categoryRepository.findById(meetingDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID: " + meetingDTO.getCategoryId()));
        meeting.setCategory(category);

        // 업데이트된 모임 정보 저장
        Meeting updatedMeeting = meetingRepository.save(meeting);
        return MeetingDTO.fromEntity(updatedMeeting);
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
    public String getMeetingIdByTitle(String title) {
        return meetingRepository.findByTitle(title)
                .map(Meeting::getId)
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

    public List<MeetingDTO> searchMeetings(String keyword) {
        return meetingRepository.findByTitleContainingIgnoreCase(keyword.trim()).stream()
                .map(MeetingDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<MeetingDTO> getMeetingsByUser(String userId) {
        return meetingRepository.findByParticipantsId(userId).stream()
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
