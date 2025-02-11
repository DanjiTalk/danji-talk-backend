package com.danjitalk.danjitalk.user.user.entity;

import com.danjitalk.danjitalk.common.entity.BaseEntity;
import com.danjitalk.danjitalk.user.user.enums.LoginMethod;
import com.danjitalk.danjitalk.user.user.enums.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SystemUser extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long systemUserId;

    private String loginId;

    private String password;

    private LocalDateTime lastLoginTime;

    @Enumerated(EnumType.STRING)
    private LoginMethod lastLoginMethod;

    @Enumerated(EnumType.STRING)
    private Role role;

}
