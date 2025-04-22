package com.danjitalk.danjitalk.infrastructure.scheduler.community;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.danjitalk.danjitalk.infrastructure.jooq.table.tables.Feed.FEED;
import static com.danjitalk.danjitalk.infrastructure.jooq.table.tables.Reaction.REACTION;
import static org.jooq.impl.DSL.count;

@Component
@Slf4j
@RequiredArgsConstructor
public class ReactionSyncJob {

    private final DSLContext dslContext;

    private static final int BATCH_SIZE = 500;

    // 30초마다 실행
    @Scheduled(cron = "0 45 0,3,6,9,12,15,18,21 * * *")
    public void run() {

        syncReactionCount();

    }

    private void syncReactionCount() {

        Field<Integer> reactionCount = count(REACTION.ID).as("reaction_count");

        List<Query> list = dslContext.select(REACTION.FEED_ID, reactionCount)
                .from(REACTION)
                .join(FEED).on(REACTION.FEED_ID.eq(FEED.ID))
                .groupBy(REACTION.FEED_ID)
                .stream()
                .map(record ->
                        (Query) dslContext.update(FEED)
                                .set(FEED.REACTION_COUNT, record.get(reactionCount))
                                .where(FEED.ID.eq(record.get(REACTION.FEED_ID))))
                .toList();

        if (list.isEmpty()) return;

        log.info("Syncing reaction count for {} feeds", list.size());

        // 500건이 넘어가면 500개씩 끊어서 사용
        if (list.size() > 500) {

            for (int i=0; i<list.size(); i += BATCH_SIZE) {
                List<Query> queries = list.subList(i, Math.min(i + BATCH_SIZE, list.size()));

                try {
                    dslContext.batch(queries).execute();
                } catch (Exception e) {
                    log.error("Batch update failed at index {}~{}, reason: {}", i, i + BATCH_SIZE, e.getMessage(), e);
                }
            }
        } else {
            try {
                dslContext.batch(list).execute();
            } catch (Exception e) {
                log.error("Batch update failed, reason: {}", e.getMessage(), e);
            }
        }

    }


}
