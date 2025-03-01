package com.danjitalk.danjitalk.domain.community.feed.service;

import com.danjitalk.danjitalk.domain.community.feed.entity.Feed;
import org.springframework.stereotype.Service;

@Service
public class FeedDomainService {

    /**
     * URL 변경
     * */
    public void changeFeedUrl(Feed feed, String feedUrl) {
        if(feedUrl == null || feedUrl.isEmpty()) {
            throw new IllegalArgumentException("FeedUrl cannot be null or empty");
        }

        feed.setFileUrl(feedUrl);
    }
}
