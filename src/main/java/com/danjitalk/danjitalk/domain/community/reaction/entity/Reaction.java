package com.danjitalk.danjitalk.domain.community.reaction.entity;

import com.danjitalk.danjitalk.domain.community.feed.entity.Feed;
import com.danjitalk.danjitalk.domain.community.reaction.enums.ReactionType;
import com.danjitalk.danjitalk.domain.user.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reaction {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ReactionType reactionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Reaction(ReactionType reactionType, Feed feed, Member member) {
        this.reactionType = reactionType;
        this.associateFeed(feed);
        this.associateMember(member);
    }
    
    /**
     * 연관관계 세팅
     * */
    public void associateFeed(Feed feed) {

        if(feed == null) return;

        this.feed = feed;
        feed.getReactionList().add(this);
    }

    public void associateMember(Member member) {
        if(member == null) return;

        this.member = member;
    }


}
