package com.danjitalk.danjitalk.api.apartment;

import com.danjitalk.danjitalk.application.apartment.ApartmentService;
import com.danjitalk.danjitalk.common.response.ApiResponse;
import com.danjitalk.danjitalk.domain.apartment.dto.ApartmentRegisterRequest;
import com.danjitalk.danjitalk.domain.apartment.dto.ApartmentRegisterResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/apartment")
@RequiredArgsConstructor
public class ApartmentController {

    private final ApartmentService apartmentService;

    @PostMapping
    public ResponseEntity<ApiResponse<ApartmentRegisterResponse>> registerApartment(
        @RequestPart("requestDto") @Valid ApartmentRegisterRequest request,
        @RequestPart(required = false) List<MultipartFile> multipartFileList
    ) {
        ApartmentRegisterResponse response = apartmentService.registerApartment(request, multipartFileList);
        return ResponseEntity.ok(ApiResponse.success(200, null, response));
    }
}
