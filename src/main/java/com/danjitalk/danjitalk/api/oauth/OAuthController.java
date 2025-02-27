package com.danjitalk.danjitalk.api.oauth;

import com.danjitalk.danjitalk.common.security.CustomMemberDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuthController {

    @GetMapping("/api/success/oauth")
    public ResponseEntity successOAuth(@AuthenticationPrincipal CustomMemberDetails memberDetails) {
        memberDetails.getEmail();
        return ResponseEntity.ok(memberDetails.getEmail());
    }
}
