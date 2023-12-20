package com.recipia.member.hexagonal.adapter.out.persistenceAdapter;

import com.recipia.member.hexagonal.application.port.out.port.JwtPort;
import com.recipia.member.hexagonal.domain.Jwt;
import com.recipia.member.hexagonal.domain.converter.JwtConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtAdapter implements JwtPort {

    private final JwtRepository jwtRepository;

    @Override
    public void save(Jwt jwt) {
        jwtRepository.save(JwtConverter.domainToEntity(jwt));
    }
}
