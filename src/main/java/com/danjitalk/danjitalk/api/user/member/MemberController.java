package com.danjitalk.danjitalk.api.user.member;

import com.danjitalk.danjitalk.application.user.member.MemberService;
import com.danjitalk.danjitalk.common.response.ApiResponse;
import com.danjitalk.danjitalk.domain.user.member.dto.request.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PutMapping("/{memberId}/profile")
    public ResponseEntity<Void> updateProfile(@PathVariable Long memberId,
                                              @RequestPart("requestDto") UpdateMemberRequestDto requestDto,
                                              @RequestPart(value = "multipartFileList", required = false) MultipartFile multipartFile) {
        memberService.updateProfile(memberId, requestDto, multipartFile);
        return ResponseEntity.ok().build();
    }
}
