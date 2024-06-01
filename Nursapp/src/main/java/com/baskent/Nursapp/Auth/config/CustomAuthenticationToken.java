package com.baskent.Nursapp.Auth.config;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class CustomAuthenticationToken extends AbstractAuthenticationToken {
    private final Long userId;
    private final String password;
    private final Long bolumId;

    public CustomAuthenticationToken(Long userId, String password, Long bolumId) {

        super(null);
        this.userId = userId;
        this.password = password;
        this.bolumId = bolumId;
        setAuthenticated(false);

    }

    @Override
    public String getCredentials() {
        return password;
    }

    @Override
    public Long getPrincipal() {
        return userId;
    }

    public Long getBolumId() {
        return bolumId;
    }
}