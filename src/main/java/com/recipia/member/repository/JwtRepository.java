package com.recipia.member.repository;

import com.recipia.member.hexagonal.adapter.out.persistence.entity.JwtEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtRepository extends JpaRepository<JwtEntity, Long> {
}
