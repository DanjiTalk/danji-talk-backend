package com.danjitalk.danjitalk.user.report.entity;

import com.danjitalk.danjitalk.common.entity.BaseEntity;
import com.danjitalk.danjitalk.user.report.enums.ReportType;
import com.danjitalk.danjitalk.user.user.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_member_id")
    private Member targetMember;              // 신고 대상 회원 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_member_id")
    private Member reportMember;              // 신고자 ID

    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    private String reportContents;

}
