package com.danjitalk.danjitalk.domain.community.feed.entity;

import com.danjitalk.danjitalk.domain.common.entity.BaseEntity;
import com.danjitalk.danjitalk.domain.community.comment.entity.Comment;
import com.danjitalk.danjitalk.domain.community.feed.enums.FeedType;
import com.danjitalk.danjitalk.domain.community.reaction.entity.Reaction;
import com.danjitalk.danjitalk.domain.user.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feed extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Setter
    private String fileUrl;

    @Enumerated(EnumType.STRING)
    private FeedType feedType;

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

    public Feed(String title, String contents, FeedType feedType) {
                this.title = title;
                this.contents = contents;
                this.feedType = feedType;
    }

    /**
     * 연관관계 세팅
     * */
    public void setMember(Member member) {
        this.member = member;
        member.getFeedList().add(this);
    }

    /**
     * 엔티티 변경 메소드
     * */
    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContents(String contents) {
        this.contents = contents;
    }

}
