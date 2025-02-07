package com.danjitalk.danjitalk.user.user.entity;

import com.danjitalk.danjitalk.common.entity.BaseEntity;
import com.danjitalk.danjitalk.common.util.CommonUtil;
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

    @Id
    @Column(length = 32, nullable = false, unique = true)
    private String systemUserId;

    private String loginId;

    private String password;

    private LocalDateTime lastLoginTime;

    @Enumerated(EnumType.STRING)
    private LoginMethod lastLoginMethod;

    @Enumerated(EnumType.STRING)
    private Role role;

    @PrePersist
    public void prePersist() {
        if(this.systemUserId == null) this.systemUserId = CommonUtil.generatedUUID();
    }
}
