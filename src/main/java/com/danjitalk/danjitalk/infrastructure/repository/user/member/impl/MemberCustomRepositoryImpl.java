package com.danjitalk.danjitalk.infrastructure.repository.user.member.impl;

import com.danjitalk.danjitalk.domain.user.member.dto.response.MyPageResponse;
import com.danjitalk.danjitalk.infrastructure.repository.user.member.MemberCustomRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.danjitalk.danjitalk.domain.user.member.entity.QMember.member;
import static com.danjitalk.danjitalk.domain.apartment.entity.QApartment.apartment;
import static com.danjitalk.danjitalk.domain.user.member.entity.QMemberApartment.memberApartment;

@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public MyPageResponse getMemberInfoById(Long memberId) {
        MyPageResponse myPageResponse = queryFactory
            .select(Projections.constructor(MyPageResponse.class,
                member.fileId,
                member.name,
                member.nickname,
                member.email,
                member.phoneNumber,

                memberApartment.id,
                apartment.id,
                apartment.name,
                apartment.region,
                apartment.location,
                memberApartment.building,
                memberApartment.unit,

                memberApartment.moveInDate,
                memberApartment.numberOfResidents
            ))
            .from(member)
            .leftJoin(memberApartment).on(member.id.eq(memberApartment.member.id))
            .leftJoin(apartment).on(memberApartment.apartment.id.eq(apartment.id))
            .where(member.id.eq(memberId))
            .fetchOne();

        String carNumbers = queryFactory
            .select(memberApartment.carNumbers)
            .from(memberApartment)
            .where(memberApartment.id.eq(myPageResponse.getMemberApartmentId()))
            .fetchOne();

        if(carNumbers != null){
            myPageResponse.setCarNumbers(carNumbers);
        }

        return myPageResponse;
    }
}
