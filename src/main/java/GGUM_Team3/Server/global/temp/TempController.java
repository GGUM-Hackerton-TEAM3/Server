package GGUM_Team3.Server.global.temp;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
@Tag(name = "테스트를 위한 임시 API. 개발 완료 후 삭제 예정")
@RequestMapping("/temp")
public class TempController { //todo: 테스트를 위함. 개발 완료 후 삭제 예정
    private final StringRedisTemplate redisTemplate;

    @GetMapping("/keys-values-ttl")
    public Map<String, Map<String, Object>> getAllKeysWithValuesAndTTL() {
        // 전체 키 가져오기
        Set<String> keys = redisTemplate.keys("*");

        // 각 키의 값과 TTL을 저장할 Map
        Map<String, Map<String, Object>> keysWithValuesAndTTL = new HashMap<>();

        if (keys != null) {
            for (String key : keys) {
                // 각 키의 TTL 가져오기 (초 단위로 TTL 조회)
                Long ttl = redisTemplate.getExpire(key, TimeUnit.SECONDS);

                // 각 키의 값을 가져옴
                Object value = redisTemplate.opsForValue().get(key);

                // 키에 대한 값과 TTL을 Map에 저장
                Map<String, Object> valueAndTTL = new HashMap<>();
                valueAndTTL.put("value", value != null ? value : "No value found");
                valueAndTTL.put("ttl", ttl != null ? ttl : -1); // TTL이 없는 경우 -1 표시

                // 전체 결과에 키별로 저장
                keysWithValuesAndTTL.put(key, valueAndTTL);
            }
        }

        return keysWithValuesAndTTL;
    }

}
