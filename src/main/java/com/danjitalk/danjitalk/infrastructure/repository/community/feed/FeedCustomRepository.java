package com.danjitalk.danjitalk.infrastructure.repository.community.feed;

import com.danjitalk.danjitalk.domain.community.feed.dto.request.GetFeedListRequestDto;
import com.danjitalk.danjitalk.domain.community.feed.dto.response.ProjectionFeedDto;
import com.danjitalk.danjitalk.domain.community.feed.entity.Feed;

import java.util.List;
import java.util.Optional;

public interface FeedCustomRepository {

    Optional<Feed> findFeedFetchJoinMemberByFeedId(Long feedId);

    Optional<List<ProjectionFeedDto>> getProjectionFeedList(GetFeedListRequestDto requestDto);
}
