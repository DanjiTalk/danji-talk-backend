package com.danjitalk.danjitalk.application.community.reaction;

import com.danjitalk.danjitalk.common.exception.BadRequestException;
import com.danjitalk.danjitalk.common.util.SecurityContextHolderUtil;
import com.danjitalk.danjitalk.infrastructure.repository.community.reaction.ReactionRepository;
import io.jsonwebtoken.lang.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReactionService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ReactionRepository reactionRepository;

    /**
     * 좋아요 INCR 처리
     * */
    public void incrReaction(Long feedId) {

        String countKey = "reaction:count:" + feedId;                               // 좋아요 수 (FEED 엔티티 업데이트 목적)
        String userSetKey = "reaction:user:" + feedId;                              // 좋아요 한 유저 ID (REACTION 엔티티에 연관관계 매핑 목적)
        String memberId = SecurityContextHolderUtil.getMemberId().toString();

        if(Objects.isEmpty(memberId)) {
            throw new BadRequestException("memberId is empty");
        }

        Boolean exists = redisTemplate.opsForSet().isMember(userSetKey, memberId);

        // 레디스에 유저가 없으면  DB에 없으면 값 증가
        if(Boolean.FALSE.equals(exists)) {
            redisTemplate.opsForValue().increment(countKey);
            redisTemplate.opsForSet().add(userSetKey, memberId);
        }
    }

    /**
     * 좋아요 DECR 처리
     * */
    public void decrReaction(Long feedId) {

        String countKey = "reaction:count:" + feedId;
        String userSetKey = "reaction:user:" + feedId;
        String removeSetKey = "reaction:removeUser:" + feedId;                      // 좋아요 해제한 유저 목록
        String memberId = SecurityContextHolderUtil.getMemberId().toString();

        if(Objects.isEmpty(memberId)) {
            throw new BadRequestException("memberId is empty");
        }

        Boolean exists = redisTemplate.opsForSet().isMember(userSetKey, memberId);
        // 있으면 TRUE, 없으면 FALSE
        Boolean alreadyRemoved = redisTemplate.opsForSet().isMember(removeSetKey, memberId);

        // 레디스에 유저가 있으면 값 감소, 레디스 SET 에서 삭제, 스케쥴링이 돌기 전 다시 해제 했을때
        if(Boolean.TRUE.equals(exists) && Boolean.FALSE.equals(alreadyRemoved)) {
            redisTemplate.opsForValue().decrement(countKey);
            redisTemplate.opsForSet().remove(userSetKey, memberId);
            redisTemplate.opsForSet().add(removeSetKey, memberId);
        } else if(Boolean.FALSE.equals(exists) && Boolean.FALSE.equals(alreadyRemoved)) {
            // 데이터가 존재하면 delete, 레디스에서 -1
            reactionRepository.findByMemberIdAndFeedId(Long.parseLong(memberId), feedId).ifPresent(reaction -> {
                redisTemplate.opsForValue().decrement(countKey);
                redisTemplate.opsForSet().add(removeSetKey, memberId);
            });
        }
    }

}
