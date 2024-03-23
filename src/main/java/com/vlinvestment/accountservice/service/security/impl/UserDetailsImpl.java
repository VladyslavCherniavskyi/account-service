package com.vlinvestment.accountservice.service.security.impl;

import com.vlinvestment.accountservice.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class UserDetailsImpl extends User implements UserDetails {

    public UserDetailsImpl(User user) {
        super(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getRoles()
        );
    }

    private static final String ROLE_PREFIX = "ROLE_";

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return super.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(ROLE_PREFIX + role.getName()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return super.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return super.getPhone();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
