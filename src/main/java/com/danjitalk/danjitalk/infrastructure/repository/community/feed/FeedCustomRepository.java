package com.danjitalk.danjitalk.infrastructure.repository.community.feed;

import com.danjitalk.danjitalk.domain.community.feed.entity.Feed;

import java.util.Optional;

public interface FeedCustomRepository {

    Optional<Feed> findFeedFetchJoinMemberByFeedId(Long feedId);
}
