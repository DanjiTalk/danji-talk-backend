package com.danjitalk.danjitalk.api.user.member;

import com.danjitalk.danjitalk.application.user.member.MemberService;
import com.danjitalk.danjitalk.common.response.ApiResponse;
import com.danjitalk.danjitalk.domain.user.member.dto.request.CheckEmailDuplicationRequest;
import com.danjitalk.danjitalk.domain.user.member.dto.request.DeleteAccountRequest;
import com.danjitalk.danjitalk.domain.user.member.dto.request.FindIdRequest;
import com.danjitalk.danjitalk.domain.user.member.dto.request.ResetPasswordRequest;
import com.danjitalk.danjitalk.domain.user.member.dto.request.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestBody SignUpRequest request) {
        memberService.signUp(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/check-email-duplication")
    public ResponseEntity<Void> checkEmailDuplication(@RequestBody CheckEmailDuplicationRequest request) {
        memberService.isEmailAvailable(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAccount(@RequestBody DeleteAccountRequest request) {
        memberService.deleteMember(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/find-id")
    public ResponseEntity<ApiResponse<String>> findAccount(@RequestBody FindIdRequest request) {
        String id = memberService.findMemberId(request);
        return ResponseEntity.ok().body(ApiResponse.success(HttpStatus.OK.value(), "아이디 찾음", id));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody ResetPasswordRequest request) {
        memberService.resetPassword(request);
        return ResponseEntity.ok().build();
    }
}
