// 좋아요 서비스
package GGUM_Team3.Server.meeting.service;

import GGUM_Team3.Server.domain.user.entity.UserEntity;
import GGUM_Team3.Server.domain.user.service.UserService;
import GGUM_Team3.Server.meeting.entity.Meeting;
import GGUM_Team3.Server.meeting.repository.MeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private UserService userService;

    /**
     * 모임에 대해 좋아요를 추가하는 메소드
     *
     * @param meetingId 좋아요를 할 모임의 ID
     * @param userId 좋아요를 하는 사용자의 ID
     * @return 현재 좋아요 수
     * @throws RuntimeException 이미 좋아요를 했거나 모임 또는 사용자를 찾을 수 없을 때 발생
     */
    public int likeMeeting(String meetingId, String userId) {
        // 모임을 ID로 조회하고 없으면 예외 발생
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new RuntimeException("Meeting not found"));

        // 사용자를 ID로 조회하고 없으면 예외 발생
        UserEntity user = userService.getById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 이미 사용자가 좋아요한 경우 예외 발생
        if (meeting.getLikes().contains(user)) {
            throw new RuntimeException("User already liked this meeting.");
        }

        // 사용자가 좋아요 리스트에 추가되고 저장
        meeting.getLikes().add(user);
        meetingRepository.save(meeting);

        // 좋아요 수 반환
        return meeting.getLikes().size();
    }

    /**
     * 모임에 대해 좋아요를 취소하는 메소드
     *
     * @param meetingId 좋아요를 취소할 모임의 ID
     * @param userId 좋아요를 취소하는 사용자의 ID
     * @return 현재 좋아요 수
     * @throws RuntimeException 좋아요를 하지 않았거나 모임 또는 사용자를 찾을 수 없을 때 발생
     */
    public int unlikeMeeting(String meetingId, String userId) {
        // 모임을 ID로 조회하고 없으면 예외 발생
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new RuntimeException("Meeting not found"));

        // 사용자를 ID로 조회하고 없으면 예외 발생
        UserEntity user = userService.getById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 사용자가 좋아요를 하지 않은 경우 예외 발생
        if (!meeting.getLikes().contains(user)) {
            throw new RuntimeException("User has not liked this meeting yet.");
        }

        // 좋아요 리스트에서 사용자 제거 후 저장
        meeting.getLikes().remove(user);
        meetingRepository.save(meeting);

        // 좋아요 수 반환
        return meeting.getLikes().size();
    }
}