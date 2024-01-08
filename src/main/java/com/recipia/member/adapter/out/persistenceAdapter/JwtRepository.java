package com.recipia.member.adapter.out.persistenceAdapter;

import com.recipia.member.adapter.out.persistence.JwtEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JwtRepository extends JpaRepository<JwtEntity, Long> {
    Optional<JwtEntity> findJwtByMemberIdAndRefreshToken(Long memberId, String refreshToken);
    void deleteByMemberId(Long memberId);
}
