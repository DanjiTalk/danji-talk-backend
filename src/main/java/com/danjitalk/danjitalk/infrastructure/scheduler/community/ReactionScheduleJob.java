package com.danjitalk.danjitalk.infrastructure.scheduler.community;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class ReactionScheduleJob {

    private final RedisTemplate<String, String> redisTemplate;
    private final EntityManager entityManager;

    // 30초마다 실행
    @Scheduled(fixedRate = 30000)
    @Transactional
    public void syncReaction() {

        bulkUpdateReactionCount();
        bulkInsertReaction();
        bulkDeleteReaction();

    }


    /**
     * 좋아요 갯수 업데이트, JOOQ 사용 Bulk Update
     * */
    private void bulkUpdateReactionCount() {

        ScanOptions scanOptions = ScanOptions.scanOptions().match("reaction:count:*").count(1000).build();
        Map<Long, Long> reactionCountMap = new HashMap<Long, Long>();

        try(Cursor<String> cursor = redisTemplate.opsForSet().scan("reaction:count", scanOptions)) {

            while (cursor.hasNext()) {
                String key = cursor.next();        // 키 값 추출

                Long feedId = Long.parseLong(key.replace("reaction:count:", ""));
                Long count = Long.parseLong(Objects.requireNonNull(redisTemplate.opsForValue().get(key)));

                reactionCountMap.put(feedId, count);
            }
        } catch(Exception e) {
            log.error(e.getMessage(), e);
        }

        // TODO :: bulk update

        // 데이터 삭제
        redisTemplate.delete(reactionCountMap.keySet()
                .stream()
                .map(feedId -> "reaction:count:" + feedId)
                .toList()
        );
    }

    /**
     * 좋아요 한 유저, JPA Persist() 사용 Bulk insert
     * */
    private void bulkInsertReaction() {
        Set<String> keys1 = redisTemplate.keys("reaction:user:*");

        // 데이터 삭제
        redisTemplate.delete(keys1);
    }

    /**
     * 좋아요 해제 한 유저, JPA Persist() 사용 bulk delete
     * */
    private void bulkDeleteReaction() {
        Set<String> keys1 = redisTemplate.keys("reaction:user:*");

        // 데이터 삭제
        redisTemplate.delete(keys1);
    }

}
