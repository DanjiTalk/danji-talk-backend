package com.danjitalk.danjitalk.api.user.member;

import com.danjitalk.danjitalk.application.user.member.MemberService;
import com.danjitalk.danjitalk.domain.user.member.dto.request.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
}
