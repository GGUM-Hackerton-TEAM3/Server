package GGUM_Team3.Server.domain.tag.hashtag.service;

import GGUM_Team3.Server.domain.tag.hashtag.entity.HashtagEntity;
import GGUM_Team3.Server.domain.tag.hashtag.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HashtagService {

    private final HashtagRepository hashtagRepository;

    public HashtagEntity createOrGetHashtag(String hashtagName) {
        try {
            return hashtagRepository.findByHashtagName(hashtagName)
                    .orElseGet(() -> hashtagRepository.save(new HashtagEntity(hashtagName)));
        } catch (Exception e) {
            log.error("해시태그 생성 중 오류가 발생했습니다: {}", e.getMessage(), e);
            throw new RuntimeException("해시태그 생성 중 오류가 발생했습니다.", e);
        }
    }

}
