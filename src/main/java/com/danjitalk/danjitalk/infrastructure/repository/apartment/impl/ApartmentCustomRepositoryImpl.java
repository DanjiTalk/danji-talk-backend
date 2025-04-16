package com.danjitalk.danjitalk.infrastructure.repository.apartment.impl;

import com.danjitalk.danjitalk.domain.search.dto.ApartmentSearchResponse;
import com.danjitalk.danjitalk.infrastructure.repository.apartment.ApartmentCustomRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

import static com.danjitalk.danjitalk.domain.apartment.entity.QApartment.apartment;

@RequiredArgsConstructor
public class ApartmentCustomRepositoryImpl implements ApartmentCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ApartmentSearchResponse> findByKeyword(String keyword) {
        return queryFactory
            .select(Projections.constructor(ApartmentSearchResponse.class,
                apartment.name,
                apartment.region,
                apartment.location,
                apartment.totalUnit,
                apartment.buildingCount,
                apartment.thumbnailFileUrl,
                // TODO 북마크 여부 체크
                Expressions.constant(Boolean.FALSE)
            ))
            .from(apartment)
            .where(keywordContains(keyword))
            .fetch();
    }

    private BooleanExpression keywordContains(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }

        return apartment.name.containsIgnoreCase(keyword)
            .or(apartment.region.containsIgnoreCase(keyword))
            .or(apartment.location.containsIgnoreCase(keyword));
    }
}
