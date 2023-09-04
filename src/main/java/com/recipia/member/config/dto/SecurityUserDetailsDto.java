package com.recipia.member.config.dto;

import com.recipia.member.dto.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Slf4j
@Getter
@AllArgsConstructor
public class SecurityUserDetailsDto implements UserDetails {

    @Delegate
    private MemberDto memberDto;
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // todo: 추후에 Member domain에 roleType field 추가
        return Collections.singletonList(new SimpleGrantedAuthority("MEMBER"));
    }

    @Override
    public String getPassword() {
        return memberDto.password();
    }

    @Override
    public String getUsername() {
        return memberDto.username();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
