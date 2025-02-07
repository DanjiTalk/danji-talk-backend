package com.danjitalk.danjitalk.community.comment.entity;

import com.danjitalk.danjitalk.common.entity.BaseEntity;
import com.danjitalk.danjitalk.common.util.CommonUtil;
import com.danjitalk.danjitalk.community.feed.entity.Feed;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @Column(length = 32, nullable = false, unique = true)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;                                     // 대댓글 구현

    @OneToMany(mappedBy = "parent")
    private List<Comment> childrenList = new ArrayList<>();     // 대댓글 구현

    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @PrePersist
    public void prePersist() {
        if(this.id == null) this.id = CommonUtil.generatedUUID();
    }
}
