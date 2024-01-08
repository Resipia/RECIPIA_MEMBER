package com.recipia.member.adapter.out.persistenceAdapter;

import com.recipia.member.adapter.out.persistence.TokenBlacklistEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklistEntity, Long> {
    Optional<TokenBlacklistEntity> findByToken(String token);
}
