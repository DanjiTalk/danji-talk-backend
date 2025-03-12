package com.danjitalk.danjitalk.application.community.reaction;

import com.danjitalk.danjitalk.common.util.SecurityContextHolderUtil;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReactionService {

    private final RedisTemplate<String, String> redisTemplate;

    public Boolean isReaction(Long feedId) {

        // TODO :: 추가 예정, 유저의 status 를 확인 후 true, false 값 반환
        String memberId = SecurityContextHolderUtil.getMemberId().toString();
        return false;
    }

    /**
     * 좋아요 INCR 처리
     * */
    public void incrReaction(Long feedId) {

        String countKey = "reaction:count:" + feedId;
        String userSetKey = "reaction:incrementUser:" + feedId;
        String userStatusKey = "reaction:status:" + feedId;
        String memberId = SecurityContextHolderUtil.getMemberId().toString();

        redisTemplate.opsForValue().increment(countKey);
        redisTemplate.opsForSet().add(userSetKey, memberId);
        redisTemplate.opsForSet().add(userStatusKey, memberId);

    }

    /**
     * 좋아요 DECR 처리
     * */
    public void decrReaction(Long feedId) {

        String countKey = "reaction:count:" + feedId;
        String userSetKey = "reaction:decrementUser:" + feedId;
        String userStatusKey = "reaction:status:" + feedId;
        String memberId = SecurityContextHolderUtil.getMemberId().toString();

        redisTemplate.opsForValue().decrement(countKey);
        redisTemplate.opsForSet().add(userSetKey, memberId);
        redisTemplate.opsForSet().remove(userStatusKey, memberId);
    }

}
