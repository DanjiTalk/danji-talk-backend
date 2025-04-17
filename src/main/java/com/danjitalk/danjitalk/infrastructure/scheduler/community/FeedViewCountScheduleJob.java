package com.danjitalk.danjitalk.infrastructure.scheduler.community;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.danjitalk.danjitalk.infrastructure.jooq.table.tables.Feed.FEED;

@Component
@Slf4j
@RequiredArgsConstructor
public class FeedViewCountScheduleJob {

    private final DSLContext dslContext;
    private final RedisTemplate<String, String> redisTemplate;

    // 1분마다 실행
    @Scheduled(fixedRate = 30000)
    @Transactional
    public void syncViewCount() {

        bulkUpdateViewCount();

    }

    private void bulkUpdateViewCount() {

        Map<Long, Long> viewCountMap = new HashMap<Long, Long>();

        redisTemplate.keys("feed:viewCount:*").forEach(key -> {
            Long feedId = Long.parseLong(key.replace("feed:viewCount:", ""));
            Long viewCount = Long.parseLong(Objects.requireNonNull(redisTemplate.opsForValue().get(key)));

            viewCountMap.put(feedId, viewCount);
            }
        );

        if(viewCountMap.isEmpty()) return;

        List<Query> list = viewCountMap.entrySet().stream().map( entry ->
            (Query) dslContext
                    .update(FEED)
                    .set(FEED.VIEW_COUNT, FEED.VIEW_COUNT.plus(entry.getValue().intValue()))
                    .where(FEED.ID.eq(entry.getKey()))
        ).toList();

        try {
            dslContext.batch(list).execute();

            // 데이터 삭제
            redisTemplate.delete(viewCountMap.keySet()
                    .stream()
                    .map(feedId -> "feed:viewCount:" + feedId)
                    .toList()
            );
        } catch (Exception e) {
            log.error("View count update failed", e);
        }

    }
}
