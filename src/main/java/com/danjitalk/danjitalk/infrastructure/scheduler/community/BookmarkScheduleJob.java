package com.danjitalk.danjitalk.infrastructure.scheduler.community;

import com.danjitalk.danjitalk.domain.bookmark.enums.BookmarkType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.danjitalk.danjitalk.infrastructure.jooq.table.tables.Bookmark.BOOKMARK;
import static com.danjitalk.danjitalk.infrastructure.jooq.table.tables.Feed.FEED;
import static org.jooq.impl.DSL.count;

@Component
@Slf4j
@RequiredArgsConstructor
public class BookmarkScheduleJob {

    private final DSLContext dslContext;

    // 30초마다 실행
    @Scheduled(fixedRate = 30000)
    @Transactional
    public void syncBookmarks() {

        bulkUpdateBookmarkCount();

    }

    /**
     * 북마크 카운트 업데이트
     * */
    private void bulkUpdateBookmarkCount() {

        Field<Integer> count = count(BOOKMARK.ID).as("count");

        Result<Record2<Long, Integer>> fetch = dslContext.select(FEED.ID, count)
                .from(FEED)
                .leftJoin(BOOKMARK).on(BOOKMARK.TYPE_ID.eq(FEED.ID).and(BOOKMARK.TYPE.eq(String.valueOf(BookmarkType.FEED))))
                .groupBy(FEED.ID, FEED.BOOKMARK_COUNT)
                .having(count(BOOKMARK.ID).ne(FEED.BOOKMARK_COUNT).or(FEED.BOOKMARK_COUNT.isNull()))
                .fetch();

        if(fetch.isEmpty()) return;

        List<Query> list = fetch.stream().map(record ->
                (Query) dslContext.update(FEED)
                        .set(FEED.BOOKMARK_COUNT, record.get(count))
                        .where(FEED.ID.eq(record.get(FEED.ID)))
        ).toList();

        dslContext.batch(list).execute();
    }
}
