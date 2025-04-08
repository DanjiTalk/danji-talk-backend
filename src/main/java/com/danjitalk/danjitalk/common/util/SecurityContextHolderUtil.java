package com.danjitalk.danjitalk.common.util;

import com.danjitalk.danjitalk.common.exception.AuthenticationException;
import com.danjitalk.danjitalk.common.exception.ForbiddenException;
import com.danjitalk.danjitalk.common.security.CustomMemberDetails;
import com.danjitalk.danjitalk.domain.user.member.entity.SystemUser;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContextHolderUtil {

    public static Long getSystemUserId() {
        CustomMemberDetails principal = getCustomMemberDetails();
        return principal.getUser().getSystemUserId();
    }

    public static SystemUser getSystemUser() {
        CustomMemberDetails principal = getCustomMemberDetails();
        return principal.getUser();
    }

    public static Long getMemberId() {
        CustomMemberDetails principal = getCustomMemberDetails();
        return principal.getUser().getMember().getId();
    }

    private static CustomMemberDetails getCustomMemberDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new AuthenticationException(401, "인증 정보가 없습니다.");
        }

        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new AuthenticationException(401, "인증 정보가 없거나 유효하지 않습니다."); // 익명 사용자는 접근할 수 없습니다.
        }

        if (!authentication.isAuthenticated()) {
            throw new AuthenticationException(401, "인증되지 않은 사용자입니다.");
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof CustomMemberDetails)) {
            throw new ForbiddenException(403, "잘못된 사용자 정보입니다.");
        }

        return (CustomMemberDetails) principal;
    }
}
