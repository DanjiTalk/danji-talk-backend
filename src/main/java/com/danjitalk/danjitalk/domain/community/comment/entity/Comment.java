package com.danjitalk.danjitalk.domain.community.comment.entity;

import com.danjitalk.danjitalk.common.exception.BadRequestException;
import com.danjitalk.danjitalk.domain.common.entity.BaseEntity;
import com.danjitalk.danjitalk.domain.community.feed.entity.Feed;
import com.danjitalk.danjitalk.domain.user.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;                                     // 대댓글 구현

    @OneToMany(mappedBy = "parent")
    private List<Comment> childrenList = new ArrayList<>();     // 대댓글 구현

    @Column(nullable = false)
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder
    public Comment(Long id, Comment parent, String contents, Feed feed, Member member) {
        this.id = id;
        this.contents = contents;
        this.associateFeed(feed);
        this.associateParentComment(parent);
        this.associateMember(member);
    }

    /**
     * 연관관계 설정
     * */
    public void associateParentComment(Comment parentComment) {

        // 부모가 없을때(첫 댓글)
        if(parentComment == null) {
            return;
        }

        if(this.equals(parentComment)) {
            throw new BadRequestException("Can't associate parent comment with self");
        }

        this.parent = parentComment;
        parentComment.getChildrenList().add(this);
    }

    public void associateFeed(Feed feed) {

        if(feed == null) {
            throw new BadRequestException("Feed is null");
        }

        this.feed = feed;
        feed.getCommentList().add(this);
    }

    public void associateMember(Member member) {
        if(member == null) {
            throw new BadRequestException("Member is null");
        }

        this.member = member;
    }

    /**
     * 엔티티 변경 메서드
     * */
    public void changeContents(String contents) {
        this.contents = contents;
    }
}
