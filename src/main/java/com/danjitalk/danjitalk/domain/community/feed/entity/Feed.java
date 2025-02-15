package com.danjitalk.danjitalk.domain.community.feed.entity;

import com.danjitalk.danjitalk.domain.common.entity.BaseEntity;
import com.danjitalk.danjitalk.domain.community.comment.entity.Comment;
import com.danjitalk.danjitalk.domain.community.reaction.entity.Reaction;
import com.danjitalk.danjitalk.domain.user.member.entity.Member;
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

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String contents;

    private Integer commentCount;

    private Integer reactionCount;

    private String fileUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "feed")
    private List<Reaction> reactionList = new ArrayList<>();

    @OneToMany(mappedBy = "feed")
    private List<Comment> commentList = new ArrayList<>();

}
