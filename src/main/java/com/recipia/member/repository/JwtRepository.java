package com.recipia.member.repository;

import com.recipia.member.domain.Jwt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtRepository extends JpaRepository<Jwt, Long> {
}
