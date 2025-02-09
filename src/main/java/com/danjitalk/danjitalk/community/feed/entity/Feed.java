package com.danjitalk.danjitalk.community.feed.entity;

import com.danjitalk.danjitalk.community.comment.entity.Comment;
import com.danjitalk.danjitalk.common.entity.BaseEntity;
import com.danjitalk.danjitalk.common.util.CommonUtil;
import com.danjitalk.danjitalk.community.reaction.entity.Reaction;
import com.danjitalk.danjitalk.user.user.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feed extends BaseEntity {

    @Id
    @Column(length = 32, nullable = false, unique = true)
    private String id;

    private String title;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String contents;

    private Integer commentCount;

    private Integer reactionCount;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "feed")
    private List<Reaction> reactionList = new ArrayList<>();

    @OneToMany(mappedBy = "feed")
    private List<Comment> commentList = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if(this.id == null) this.id = CommonUtil.generatedUUID();
    }

}
