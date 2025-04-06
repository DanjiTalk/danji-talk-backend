package com.danjitalk.danjitalk.common.util;

import com.danjitalk.danjitalk.common.exception.AuthenticationException;
import com.danjitalk.danjitalk.common.exception.ForbiddenException;
import com.danjitalk.danjitalk.common.security.CustomMemberDetails;
import com.danjitalk.danjitalk.domain.user.member.entity.SystemUser;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityContextHolderUtil {

    public static Long getSystemUserId() {
        return getCustomMemberDetails().map(details -> details.getUser().getSystemUserId())
                .orElseThrow(() -> new AuthenticationException(401, "로그인된 사용자가 아닙니다."));
    }

    public static SystemUser getSystemUser() {
        return getCustomMemberDetails().map(CustomMemberDetails::getUser)
                .orElseThrow(() -> new AuthenticationException(401, "로그인된 사용자가 아닙니다."));
    }

    public static Long getMemberId() {
        return getCustomMemberDetails().map(details ->
                details.getUser()
                        .getMember()
                        .getId()
        ).orElseThrow(() -> new AuthenticationException(401, "로그인된 사용자가 아닙니다."));
    }

    /**
     * Optional 반환: 인증되지 않은 사용자도 접근 가능 (서비스 단에서 처리)
     */

    public static Optional<Long> getSystemUserIdOptional() {
        return getCustomMemberDetails().map(details -> details.getUser().getSystemUserId());
    }

    public static Optional<SystemUser> getSystemUserOptional() {
        return getCustomMemberDetails().map(CustomMemberDetails::getUser);
    }

    public static Optional<Long> getMemberIdOptional() {
        return getCustomMemberDetails().map(details ->
                details.getUser()
                        .getMember()
                        .getId()
        );
    }

    private static Optional<CustomMemberDetails> getCustomMemberDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            log.warn("인증 객체가 존재하지 않습니다.");
            throw new AuthenticationException(401, "인증 정보가 없습니다.");
        }

        if (authentication instanceof AnonymousAuthenticationToken) {
            log.warn("익명 회원 인증 요청");
            return Optional.empty();
            // 스프링 시큐리티는 로그인하지 않은 사용자도 AnonymousAuthenticationToken으로 인증 객체를 생성함
            // 따라서 익명 사용자의 경우 예외를 바로 던지면, 인증이 필수가 아닌 요청에서도 서비스 로직 도중 예외가 발생할 수 있음
            // → 예: Optional로 처리해야 할 API에서도 예외가 발생하게 됨
            // 그래서 여기서는 예외를 던지지 않고 Optional.empty()를 반환하여 상황에 따라 유연하게 처리 가능하도록 함
            // throw new AuthenticationException(401, "인증 정보가 없거나 유효하지 않습니다."); // 익명 사용자는 접근할 수 없습니다.
        }

        if (!authentication.isAuthenticated()) {
            log.warn("인증 객체는 존재하지만 인증되지 않은 사용자입니다.");
            throw new AuthenticationException(401, "인증되지 않은 사용자입니다.");
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof CustomMemberDetails)) {  // instanceof 는 null에 대해 false
            throw new ForbiddenException(403, "잘못된 사용자 정보입니다.");
        }

        return Optional.of((CustomMemberDetails) principal);
    }
}
