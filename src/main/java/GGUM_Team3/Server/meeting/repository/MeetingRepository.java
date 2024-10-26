// 모임 리포지토리
package GGUM_Team3.Server.meeting.repository;

import GGUM_Team3.Server.meeting.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, String> {
    List<Meeting> findByTitleContaining(String keyword);

    Optional<Meeting> findByTitle(String title);

    List<Meeting> findByTitleContainingIgnoreCase(String keyword);
}
