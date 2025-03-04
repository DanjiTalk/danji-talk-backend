package com.danjitalk.danjitalk.infrastructure.repository.apartment;

import com.danjitalk.danjitalk.domain.apartment.entity.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Long> {
}
