// 모임 서비스
package GGUM_Team3.Server.meeting.service;

import GGUM_Team3.Server.domain.user.entity.UserEntity;
import GGUM_Team3.Server.domain.user.service.UserService;
import GGUM_Team3.Server.meeting.DTO.MeetingDTO;
import GGUM_Team3.Server.meeting.entity.Meeting;
import GGUM_Team3.Server.meeting.repository.MeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MeetingService {
    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private UserService userService;

    @PreAuthorize("isAuthenticated()") // 인증된 사용자만 접근가능
    public MeetingDTO createMeeting(MeetingDTO meetingDTO) {
        Meeting meeting = Meeting.builder()
                .title(meetingDTO.getTitle())
                .description(meetingDTO.getDescription())
                .maxParticipants(meetingDTO.getMaxParticipants())
                .startTime(meetingDTO.getStartTime())
                .notice(meetingDTO.getNotice()) // 공지 태그 추가
                .tags(meetingDTO.getTags()) // 태그 필드 추가
                .build();
        Meeting savedMeeting = meetingRepository.save(meeting);
        return MeetingDTO.fromEntity(savedMeeting);
    }

    public List<MeetingDTO> getMeetingsByCategory(String categoryId) {
        return meetingRepository.findByCategoryId(categoryId).stream()
                .map(MeetingDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<MeetingDTO> getMeetingsByRegion(String region) {
        return meetingRepository.findByRegion(region).stream()
                .map(MeetingDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<MeetingDTO> getMeetingsByTag(String tag) {
        return meetingRepository.findByTagsContaining(tag).stream()
                .map(MeetingDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<MeetingDTO> getTopLikedMeetings() {
        return meetingRepository.findAllByOrderByLikesCountDesc().stream()
                .map(MeetingDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<MeetingDTO> getMeetingsLikedByUser(String userId) {
        UserEntity user = userService.getById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return meetingRepository.findByLikesContaining(user).stream()
                .map(MeetingDTO::fromEntity)
                .collect(Collectors.toList());
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


    // title에서 keyword 있는지 확인
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

        // 현재 시간이 모임 시작 시간 이후라면 가입 불가 처리
        if (meeting.getStartTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Cannot join meeting after it has started.");
        }

        // 이미 모임에 가입된 경우 처리
        if (meeting.getParticipants().contains(user)) {
            throw new RuntimeException("User already joined the meeting.");
        }

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
