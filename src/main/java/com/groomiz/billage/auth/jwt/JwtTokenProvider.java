package com.groomiz.billage.auth.jwt;

import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private final JwtUtil jwtUtil;

    public JwtTokenProvider(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public String createAccessToken(String username, String role) {
        return jwtUtil.createJwt("AccessToken", username, role, 600000L);
    }

    public String createRefreshToken(String username, String role) {
        return jwtUtil.createJwt("RefreshToken", username, role, 86400000L);
    }
}
