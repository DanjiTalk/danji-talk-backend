package com.danjitalk.danjitalk.domain.user.member.entity;

import com.danjitalk.danjitalk.domain.common.entity.BaseEntity;
import com.danjitalk.danjitalk.domain.user.member.enums.LoginMethod;
import com.danjitalk.danjitalk.domain.user.member.enums.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SystemUser extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long systemUserId;

    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    private String loginId;

    private String password;

    private LocalDateTime lastLoginTime;

    @Enumerated(EnumType.STRING)
    private LoginMethod lastLoginMethod;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public SystemUser(Role role, LoginMethod lastLoginMethod, LocalDateTime lastLoginTime, String password,
        String loginId, Member member) {
        this.role = role;
        this.lastLoginMethod = lastLoginMethod;
        this.lastLoginTime = lastLoginTime;
        this.password = password;
        this.loginId = loginId;
        this.member = member;
    }
}
