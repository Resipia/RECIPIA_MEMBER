package com.recipia.member.hexagonal.adapter.out.persistenceAdapter;

import com.recipia.member.hexagonal.adapter.out.persistence.JwtEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtRepository extends JpaRepository<JwtEntity, Long> {
}
