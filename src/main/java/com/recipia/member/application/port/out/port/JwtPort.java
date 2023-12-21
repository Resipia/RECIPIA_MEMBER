package com.recipia.member.application.port.out.port;

import com.recipia.member.domain.Jwt;
import org.springframework.stereotype.Component;

public interface JwtPort {
    void save(Jwt jwt);
}
