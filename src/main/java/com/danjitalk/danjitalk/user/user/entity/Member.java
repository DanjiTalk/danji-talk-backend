package com.danjitalk.danjitalk.user.user.entity;

import com.danjitalk.danjitalk.chat.entity.ChatroomMemberMapping;
import com.danjitalk.danjitalk.common.entity.BaseEntity;
import com.danjitalk.danjitalk.community.feed.entity.Feed;
import com.danjitalk.danjitalk.user.report.entity.Report;
import com.danjitalk.danjitalk.user.user.enums.Gender;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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

    private LocalDate birthDate;                            // 생년월일

    private Integer age;                                    // 나이

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

}
