package com.recipia.member.adapter.out.persistenceAdapter;

import com.recipia.member.application.port.out.port.JwtPort;
import com.recipia.member.domain.Jwt;
import com.recipia.member.domain.converter.JwtConverter;
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
