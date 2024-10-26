package GGUM_Team3.Server.tag.hashtag.repository;

import GGUM_Team3.Server.tag.hashtag.entity.HashtagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface HashtagRepository extends JpaRepository<HashtagEntity, UUID> {
    Optional<HashtagEntity> findByHashtagName(String hashtagName);
}
