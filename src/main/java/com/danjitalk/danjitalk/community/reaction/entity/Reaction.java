package com.danjitalk.danjitalk.community.reaction.entity;

import com.danjitalk.danjitalk.common.util.CommonUtil;
import com.danjitalk.danjitalk.community.feed.entity.Feed;
import com.danjitalk.danjitalk.community.reaction.enums.ReactionType;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Reaction {

    @Id
    @Column(length = 32, nullable = false, unique = true)
    private String id;

    @Enumerated(EnumType.STRING)
    private ReactionType reactionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @PrePersist
    public void prePersist() {
        if(this.id == null) this.id = CommonUtil.generatedUUID();
    }
}
