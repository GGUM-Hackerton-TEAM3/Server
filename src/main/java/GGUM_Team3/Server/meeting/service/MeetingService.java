// 모임 서비스
package GGUM_Team3.Server.meeting.service;

import GGUM_Team3.Server.domain.user.entity.UserEntity;
import GGUM_Team3.Server.domain.user.service.UserService;
import GGUM_Team3.Server.meeting.DTO.MeetingDTO;
import GGUM_Team3.Server.meeting.entity.Meeting;
import GGUM_Team3.Server.meeting.repository.MeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MeetingService {
    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private UserService userService;

    public MeetingDTO createMeeting(MeetingDTO meetingDTO) {
        Meeting meeting = Meeting.builder()
                .title(meetingDTO.getTitle())
                .description(meetingDTO.getDescription())
                .maxParticipants(meetingDTO.getMaxParticipants())
                .startTime(meetingDTO.getStartTime())
                .build();
        Meeting savedMeeting = meetingRepository.save(meeting);
        return MeetingDTO.fromEntity(savedMeeting);
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

