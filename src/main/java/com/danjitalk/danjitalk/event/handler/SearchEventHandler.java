package com.danjitalk.danjitalk.event.handler;

import com.danjitalk.danjitalk.domain.apartment.dto.RecentComplexViewedDTO;
import com.danjitalk.danjitalk.event.dto.RecentComplexViewedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class SearchEventHandler {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 저장해야 하는 데이터 id, 썸네일, 이름, 위치, 세대, 동수, 북마크(배제)?
     */
    @TransactionalEventListener
//    @Transactional(propagation = Propagation.REQUIRES_NEW) 레디스 작업만 있어서 필요 없음
    public void handleEvent(RecentComplexViewedEvent event) {
        Long memberId = event.memberId();

        String key = "recent:apartment:member:" + memberId;

        RecentComplexViewedDTO recentComplexViewedDTO = new RecentComplexViewedDTO(
                event.id(),
                event.name(),
                event.region(),
                event.location(),
                event.totalUnit(),
                event.buildingCount(),
                event.thumbnailFileUrl()
        );

        redisTemplate.opsForList().remove(key, 0, recentComplexViewedDTO);
        redisTemplate.opsForList().leftPush(key, recentComplexViewedDTO);
        redisTemplate.opsForList().trim(key, 0, 4);
    }
}
