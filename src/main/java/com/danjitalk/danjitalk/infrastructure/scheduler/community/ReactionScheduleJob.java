package com.danjitalk.danjitalk.infrastructure.scheduler.community;

import com.danjitalk.danjitalk.domain.community.reaction.enums.ReactionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.danjitalk.danjitalk.infrastructure.jooq.table.Tables.FEED;
import static com.danjitalk.danjitalk.infrastructure.jooq.table.Tables.REACTION;

@Component
@Slf4j
@RequiredArgsConstructor
public class ReactionScheduleJob {

    private final RedisTemplate<String, String> redisTemplate;
    private final DSLContext dslContext;

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
     * 현재 데이터가 많아도 배치사이즈를 두지않음. 나중에 필요 시 배치사이즈 이용
     * */
    private void bulkUpdateReactionCount() {

        Map<Long, Long> reactionCountMap = new HashMap<Long, Long>();

        Set<String> keys = redisTemplate.keys("reaction:count:*");

        if(keys.isEmpty()) return;

        for (String key : keys) {
            Long feedId = Long.parseLong(key.replace("reaction:count:", ""));
            Long count = Long.parseLong(Objects.requireNonNull(redisTemplate.opsForValue().get(key)));

            reactionCountMap.put(feedId, count);
        }

        // Bulk Update
        List<Query> list = reactionCountMap.entrySet().stream()
                .map(entry -> (Query) dslContext
                        .update(FEED)
                        .set(FEED.REACTION_COUNT, entry.getValue().intValue())
                        .where(FEED.ID.eq(entry.getKey()))
                ).toList();

        try {
            dslContext.batch(list).execute();

            // 데이터 삭제
            redisTemplate.delete(reactionCountMap.keySet()
                    .stream()
                    .map(feedId -> "reaction:count:" + feedId)
                    .toList()
            );
        } catch (Exception e) {
            log.error("reaction count update failed", e);
        }
    }

    /**
     * 좋아요 한 유저, JPA Persist() 사용 Bulk insert
     * */
    public void bulkInsertReaction() {
        Set<String> keys1 = redisTemplate.keys("reaction:user:*");

        if(keys1.isEmpty()) return;

        List<Query> list = new ArrayList<>();

        keys1.forEach(key -> {
            Long feedId = Long.parseLong(key.replace("reaction:user:", ""));
            Long memberId = Long.parseLong(Objects.requireNonNull(redisTemplate.opsForValue().get(key)));

            list.add(
                    dslContext.insertInto(REACTION)
                            .set(REACTION.REACTION_TYPE, ReactionType.LIKE)
                            .set(REACTION.FEED_ID, feedId)
                            .set(REACTION.MEMBER_ID, memberId)
            );
        });

        dslContext.batch(list).execute();

        // 데이터 삭제
        redisTemplate.delete(keys1
                .stream()
                .toList()
        );
    }

    /**
     * 좋아요 해제 한 유저, JPA Persist() 사용 bulk delete
     * */
    private void bulkDeleteReaction() {
        Set<String> keys1 = redisTemplate.keys("reaction:user:*");

        if(keys1.isEmpty()) return;

        // 데이터 삭제
        redisTemplate.delete(keys1);
    }

}
