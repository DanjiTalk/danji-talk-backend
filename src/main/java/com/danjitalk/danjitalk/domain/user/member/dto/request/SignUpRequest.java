package com.danjitalk.danjitalk.domain.user.member.dto.request;

import java.time.LocalDate;

public record SignUpRequest(
    String email,
    String name,
    LocalDate birthDate,
    Integer age,
    String phoneNumber,
    Boolean notificationEnabled,
    String fileId,
    String password
) {

}
