// 모임 리포지토리
package GGUM_Team3.Server.meeting.repository;

import GGUM_Team3.Server.domain.user.entity.UserEntity;
import GGUM_Team3.Server.meeting.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, String> {
    List<Meeting> findByTitleContaining(String keyword);
    List<Meeting> findByCategoryId(String categoryId); // 카테고리 아이디 기반
    List<Meeting> findByRegion(String region); // 지역 기반 모임
    List<Meeting> findByTagsContaining(String tag); // 태그 포함한 모임
    List<Meeting> findAllByOrderByLikesCountDesc(); // 좋아요 수가 많은 모임을 우선으로 정렬
    List<Meeting> findByLikesContaining(UserEntity user); // 사용자가 좋아요한 모임
}
