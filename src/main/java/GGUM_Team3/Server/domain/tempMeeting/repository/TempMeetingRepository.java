package GGUM_Team3.Server.domain.tempMeeting.repository;

import GGUM_Team3.Server.domain.tempMeeting.entity.TempMeetingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TempMeetingRepository extends JpaRepository<TempMeetingEntity, UUID> {
    // 타이틀로 TempMeetingEntity를 찾는 메서드
    List<TempMeetingEntity> findByTempMeetingTitle(String tempMeetingTitle);
}
