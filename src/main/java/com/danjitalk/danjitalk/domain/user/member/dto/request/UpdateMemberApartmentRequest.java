package com.danjitalk.danjitalk.domain.user.member.dto.request;

import java.time.LocalDate;
import java.util.List;

public record UpdateMemberApartmentRequest(
    Long apartmentId,
    String building,
    String unit,
    LocalDate moveInDate,
    Integer numberOfResidents,
    List<String> carNumbers
) {

}
