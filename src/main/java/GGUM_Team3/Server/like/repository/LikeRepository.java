package GGUM_Team3.Server.like.repository;

import GGUM_Team3.Server.like.entity.LikeEntity;
import GGUM_Team3.Server.meeting.entity.Meeting;
import GGUM_Team3.Server.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
    Optional<LikeEntity> findByMeetingAndUser(Meeting meeting, UserEntity user);
    int countByMeeting(Meeting meeting);
}
