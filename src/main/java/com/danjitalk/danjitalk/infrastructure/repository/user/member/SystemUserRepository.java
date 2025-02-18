package com.danjitalk.danjitalk.infrastructure.repository.user.member;

import com.danjitalk.danjitalk.domain.user.member.entity.SystemUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemUserRepository extends JpaRepository<SystemUser, Long> {

    Optional<SystemUser> findByLoginId(String email);
}
