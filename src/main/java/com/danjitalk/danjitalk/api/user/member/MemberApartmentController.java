package com.danjitalk.danjitalk.api.user.member;

import com.danjitalk.danjitalk.application.user.member.MemberApartmentService;
import com.danjitalk.danjitalk.common.response.ApiResponse;
import com.danjitalk.danjitalk.domain.user.member.dto.request.CreateMemberApartmentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberApartmentController {

    private final MemberApartmentService memberApartmentService;

    @PostMapping("/member-apartments")
    public ResponseEntity<ApiResponse<Void>> registerMemberApartment(@RequestBody CreateMemberApartmentRequest request) {
        memberApartmentService.registerMemberApartment(request);
        return ResponseEntity.ok(ApiResponse.success(200, null, null));
    }
}
