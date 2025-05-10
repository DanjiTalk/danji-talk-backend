package com.danjitalk.danjitalk.infrastructure.repository.user.member;

import com.danjitalk.danjitalk.domain.apartment.entity.Apartment;
import com.danjitalk.danjitalk.domain.user.member.entity.Member;
import com.danjitalk.danjitalk.domain.user.member.entity.MemberApartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberApartmentRepository extends JpaRepository<MemberApartment, Long> {
    boolean existsByMemberAndApartment(Member member, Apartment apartment);
}
