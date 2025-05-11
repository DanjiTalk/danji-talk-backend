package com.danjitalk.danjitalk.application.user.member;

import com.danjitalk.danjitalk.common.exception.ConflictException;
import com.danjitalk.danjitalk.common.exception.DataNotFoundException;
import com.danjitalk.danjitalk.common.exception.ForbiddenException;
import com.danjitalk.danjitalk.common.util.SecurityContextHolderUtil;
import com.danjitalk.danjitalk.domain.apartment.entity.Apartment;
import com.danjitalk.danjitalk.domain.user.member.dto.request.CreateMemberApartmentRequest;
import com.danjitalk.danjitalk.domain.user.member.entity.Member;
import com.danjitalk.danjitalk.domain.user.member.entity.MemberApartment;
import com.danjitalk.danjitalk.infrastructure.repository.apartment.ApartmentRepository;
import com.danjitalk.danjitalk.infrastructure.repository.user.member.MemberApartmentRepository;
import com.danjitalk.danjitalk.infrastructure.repository.user.member.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberApartmentService {

    private final MemberApartmentRepository memberApartmentRepository;
    private final MemberRepository memberRepository;
    private final ApartmentRepository apartmentRepository;

    @Transactional
    public void registerMemberApartment(CreateMemberApartmentRequest request) {
        Long currentMemberId = SecurityContextHolderUtil.getMemberId();
        Member member = memberRepository.findById(currentMemberId).orElseThrow(DataNotFoundException::new);
        Apartment apartment = apartmentRepository.findById(request.apartmentId()).orElseThrow(DataNotFoundException::new);

        boolean exists = memberApartmentRepository.existsByMemberAndApartment(member, apartment);
        if (exists) {
            throw new ConflictException("이미 등록된 아파트입니다.");
        }

        List<String> carNumbers = request.carNumbers();

        String carNumbersToString =
                (carNumbers != null && !carNumbers.isEmpty())
                        ? String.join(",", carNumbers)
                        : "";

        MemberApartment memberApartment = MemberApartment.builder()
                .apartment(apartment)
                .member(member)
                .building(request.building())
                .unit(request.unit())
                .moveInDate(request.moveInDate())
                .numberOfResidents(request.numberOfResidents())
                .carNumbers(carNumbersToString)
                .build();

        memberApartmentRepository.save(memberApartment);
    }

    @Transactional
    public void deleteMemberApartment(Long memberApartmentId) {
        Long currentMemberId = SecurityContextHolderUtil.getMemberId();
        MemberApartment memberApartment = memberApartmentRepository.findById(memberApartmentId).orElseThrow(DataNotFoundException::new);

        if(!currentMemberId.equals(memberApartment.getMember().getId())) {
            throw new ForbiddenException(403, "다른 사용자의 정보에 접근할 수 없습니다.");
        }

        memberApartmentRepository.deleteById(memberApartmentId);
    }
}
