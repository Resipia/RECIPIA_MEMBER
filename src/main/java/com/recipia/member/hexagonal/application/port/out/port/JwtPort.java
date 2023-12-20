package com.recipia.member.hexagonal.application.port.out.port;

import com.recipia.member.hexagonal.domain.Jwt;
import org.springframework.stereotype.Component;

public interface JwtPort {
    void save(Jwt jwt);
}
