package com.danjitalk.danjitalk.domain.community.feed.entity;

import com.danjitalk.danjitalk.domain.apartment.entity.Apartment;
import com.danjitalk.danjitalk.domain.common.entity.BaseEntity;
import com.danjitalk.danjitalk.domain.community.comment.entity.Comment;
import com.danjitalk.danjitalk.domain.community.feed.enums.FeedType;
import com.danjitalk.danjitalk.domain.community.reaction.entity.Reaction;
import com.danjitalk.danjitalk.domain.user.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne(fetch = FetchType.LAZY)
    private Apartment apartment;

    @OneToMany(mappedBy = "feed")
    private List<Reaction> reactionList = new ArrayList<>();

    @OneToMany(mappedBy = "feed")
    private List<Comment> commentList = new ArrayList<>();

    // entity save 에서 사용
    @Builder
    public Feed(String title, String contents, FeedType feedType, String fileUrl, Member member, Apartment apartment) {
                this.title = title;
                this.contents = contents;
                this.feedType = feedType;
                this.fileUrl = fileUrl;
                this.associateMember(member);
                this.associateApartment(apartment);
    }

    /**
     * 연관관계 세팅
     * */
    public void associateMember(Member member) {
        if( member == null ) return;

        this.member = member;
        member.getFeedList().add(this);
    }

    public void associateApartment(Apartment apartment) {
        if( apartment == null ) return;

        this.apartment = apartment;
        apartment.getFeedList().add(this);
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
