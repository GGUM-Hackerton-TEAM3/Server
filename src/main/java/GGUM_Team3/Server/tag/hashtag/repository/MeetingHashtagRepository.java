package GGUM_Team3.Server.tag.hashtag.repository;

import GGUM_Team3.Server.tag.hashtag.entity.MeetingHashtagEntity;
import GGUM_Team3.Server.meeting.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MeetingHashtagRepository extends JpaRepository<MeetingHashtagEntity, UUID> {
    List<MeetingHashtagEntity> findByHashtagEntity_HashtagName(String hashtagName);

    List<MeetingHashtagEntity> findByMeeting(Meeting meeting);
}
