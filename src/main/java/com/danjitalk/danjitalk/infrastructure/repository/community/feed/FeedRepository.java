package com.danjitalk.danjitalk.infrastructure.repository.community.feed;

import com.danjitalk.danjitalk.domain.community.feed.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {
}
