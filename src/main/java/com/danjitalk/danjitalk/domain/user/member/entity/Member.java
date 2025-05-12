package com.danjitalk.danjitalk.domain.user.member.entity;

import com.danjitalk.danjitalk.domain.chat.entity.ChatroomMemberMapping;
import com.danjitalk.danjitalk.domain.common.entity.BaseEntity;
import com.danjitalk.danjitalk.domain.community.feed.entity.Feed;
import com.danjitalk.danjitalk.domain.user.report.entity.Report;
import com.danjitalk.danjitalk.domain.user.member.enums.Gender;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;                                   // 이메일

    private String name;                                    // 이름

    private String nickname;                                // 닉네임

    private String phoneNumber;                             // 휴대폰 번호

    private Boolean notificationEnabled;                    // 메일 수신 여부

    private Boolean isRestricted;                           // 제재 여부

    private LocalDateTime restrictionTime;                  // 제재 시간

    private String fileId;                                  // 프로필 사진

    @Enumerated(EnumType.STRING)
    private Gender gender;                                  // 성별 (male, female)

    @OneToMany(mappedBy = "member")
    private List<Feed> feedList = new ArrayList<>();                // 게시글 연관관계

    @OneToMany(mappedBy = "targetMember")
    private List<Report> targetMemberList = new ArrayList<>();      // 신고 연관관계

    @OneToMany(mappedBy = "reportMember")
    private List<Report> reportMemberList = new ArrayList<>();      // 신고 연관관계

    @OneToMany(mappedBy = "member")
    private List<ChatroomMemberMapping> chatroomMemberList = new ArrayList<>();

    @Builder
    public Member(String email, String name, String phoneNumber,
        Boolean notificationEnabled, Boolean isRestricted, LocalDateTime restrictionTime, String fileId,
        Gender gender, String nickname) {
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.notificationEnabled = notificationEnabled;
        this.isRestricted = isRestricted;
        this.restrictionTime = restrictionTime;
        this.fileId = fileId;
        this.gender = gender;
    }

    public void updateProfile(String name, String nickname, String phoneNumber) {
        this.name = name;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
    }
}
