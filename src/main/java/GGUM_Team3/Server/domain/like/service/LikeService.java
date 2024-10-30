package GGUM_Team3.Server.domain.like.service;

import GGUM_Team3.Server.domain.like.dto.LikeDTO;
import GGUM_Team3.Server.domain.like.entity.LikeEntity;
import GGUM_Team3.Server.domain.like.repository.LikeRepository;
import GGUM_Team3.Server.domain.meeting.entity.Meeting;
import GGUM_Team3.Server.domain.meeting.repository.MeetingRepository;
import GGUM_Team3.Server.domain.user.entity.UserEntity;
import GGUM_Team3.Server.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private UserService userService;

    public void likeMeeting(LikeDTO likeDTO) {
        Meeting meeting = meetingRepository.findById(likeDTO.getMeetingId())
                .orElseThrow(() -> new RuntimeException("Meeting not found"));

        UserEntity user = userService.getById(likeDTO.getUserId());

        // 이미 좋아요가 존재하는지 확인
        if (likeRepository.findByMeetingAndUser(meeting, user).isPresent()) {
            throw new RuntimeException("User already liked this meeting.");
        }

        LikeEntity likeEntity = LikeEntity.builder()
                .meeting(meeting)
                .user(user)
                .build();
        likeRepository.save(likeEntity);
    }

    public void unlikeMeeting(LikeDTO likeDTO) {
        Meeting meeting = meetingRepository.findById(likeDTO.getMeetingId())
                .orElseThrow(() -> new RuntimeException("Meeting not found"));

        UserEntity user = userService.getById(likeDTO.getUserId());

        // 좋아요가 존재하는지 확인
        LikeEntity likeEntity = likeRepository.findByMeetingAndUser(meeting, user)
                .orElseThrow(() -> new RuntimeException("Like not found"));

        likeRepository.delete(likeEntity);
    }
}
