package com.danjitalk.danjitalk.infrastructure.repository.community.reaction;

import com.danjitalk.danjitalk.domain.community.reaction.entity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {

    Optional<Reaction> findByMemberIdAndFeedId(Long memberId, Long feedId);
}
