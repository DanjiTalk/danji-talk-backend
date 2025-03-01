package com.danjitalk.danjitalk.common.util;

import com.danjitalk.danjitalk.common.security.CustomMemberDetails;
import com.danjitalk.danjitalk.domain.user.member.entity.SystemUser;
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
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("authentication is not valid");
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof CustomMemberDetails)) {
            throw new IllegalStateException("principal is not valid");
        }

        return (CustomMemberDetails) principal;
    }
}
